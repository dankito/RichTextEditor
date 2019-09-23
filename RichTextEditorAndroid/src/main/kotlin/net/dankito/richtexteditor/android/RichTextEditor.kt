package net.dankito.richtexteditor.android

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.webkit.WebChromeClient
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.android.extensions.hasDarkRichTextEditorTheme
import net.dankito.richtexteditor.callback.DidHtmlChangeListener
import net.dankito.richtexteditor.callback.GetCurrentHtmlCallback
import net.dankito.richtexteditor.listener.EditorLoadedListener
import net.dankito.richtexteditor.model.DownloadImageConfig
import net.dankito.richtexteditor.model.Theme
import net.dankito.utils.android.extensions.asActivity
import net.dankito.utils.android.extensions.showKeyboard
import net.dankito.utils.android.keyboard.KeyboardState
import net.dankito.utils.android.permissions.IPermissionsService
import org.slf4j.LoggerFactory
import java.io.File


open class RichTextEditor : FullscreenWebView {

    companion object {
        const val DefaultHtml = JavaScriptExecutorBase.DefaultHtml

        const val HtmlFileToRestoreStateParamId = "RICH_TEXT_EDITOR_HTML_FILE_TO_RESTORE"
        const val BaseUrlToRestoreStateParamId = "RICH_TEXT_EDITOR_BASE_URL_TO_RESTORE"
        const val BaseClassStateParamId = "BASE_CLASS_STATE_TO_RESTORE"

        private val log = LoggerFactory.getLogger(RichTextEditor::class.java)
    }


    constructor(context: Context) : super(context) { initEditor(context, null) }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) { initEditor(context, attrs) }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { initEditor(context, attrs) }



    val javaScriptExecutor = AndroidJavaScriptExecutor(this)

    var permissionsService: IPermissionsService? = null

    var downloadImageConfig: DownloadImageConfig? = null

    protected var isLoaded = false

    protected var paddingToSetOnStart: Rect? = null


    protected val keyboardState = KeyboardState()

    protected val editorLoadedListeners = ArrayList<() -> Unit>()

    protected val onTouchListeners = ArrayList<(MotionEvent) -> Unit>()


    @SuppressLint("SetJavaScriptEnabled")
    protected open fun initEditor(context: Context, attributes: AttributeSet?) {
        attributes?.let { applyAttributes(context, it) }

        this.isHorizontalScrollBarEnabled = false
        this.settings.javaScriptEnabled = true

        this.webChromeClient = WebChromeClient()

        this.settings.defaultTextEncodingName = "UTF-8" // otherwise non ASCII text doesn't get displayed correctly
        this.settings.domStorageEnabled = true // otherwise images won't load
        this.settings.loadsImagesAutomatically = true
        this.settings.setSupportZoom(true)
        this.settings.builtInZoomControls = true
        this.settings.displayZoomControls = false

        javaScriptExecutor.addLoadedListener {
            editorLoaded(context)
        }
    }

    protected open fun applyAttributes(context: Context, attrs: AttributeSet) {
        val attrsArray = intArrayOf(android.R.attr.gravity)
        val ta = context.obtainStyledAttributes(attrs, attrsArray)

        val gravity = ta.getInt(0, View.NO_ID)
        when (gravity) {
            Gravity.LEFT -> executeEditorJavaScriptFunction("setTextAlign(\"left\")")
            Gravity.RIGHT -> executeEditorJavaScriptFunction("setTextAlign(\"right\")")
            Gravity.TOP -> executeEditorJavaScriptFunction("setVerticalAlign(\"top\")")
            Gravity.BOTTOM -> executeEditorJavaScriptFunction("setVerticalAlign(\"bottom\")")
            Gravity.CENTER_VERTICAL -> executeEditorJavaScriptFunction("setVerticalAlign(\"middle\")")
            Gravity.CENTER_HORIZONTAL -> executeEditorJavaScriptFunction("setTextAlign(\"center\")")
            Gravity.CENTER -> {
                executeEditorJavaScriptFunction("setVerticalAlign(\"middle\")")
                executeEditorJavaScriptFunction("setTextAlign(\"center\")")
            }
        }

        ta.recycle()
    }

    protected open fun editorLoaded(context: Context) {
        isLoaded = true

        paddingToSetOnStart?.let {
            setPadding(it.left, it.top, it.right, it.bottom)
            paddingToSetOnStart = null
        }

        context.asActivity()?.let { activity ->
            if (activity.hasDarkRichTextEditorTheme) {
                setTheme(Theme.Dark)
            }

            activity.runOnUiThread {
                setInitialValues()

                callEditorLoadedListeners()
            }
        }
    }

    protected open fun setInitialValues() {
        setEditorFontFamily(getDefaultFontFamily())
    }

    protected open fun getDefaultFontFamily() = "serif"


    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()

        bundle.putParcelable(BaseClassStateParamId, super.onSaveInstanceState())

        saveEditorState(bundle)

        return bundle
    }

    private fun saveEditorState(bundle: Bundle) {
        val stateFile = File.createTempFile("RichTextEditorState", ".html")

        stateFile.writeText(getCachedHtml())

        bundle.putString(HtmlFileToRestoreStateParamId, stateFile.absolutePath)

        javaScriptExecutor.baseUrl?.let {
            bundle.putString(BaseUrlToRestoreStateParamId, it)
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        (state as? Bundle)?.let { bundle ->
            bundle.getParcelable<Parcelable>(BaseClassStateParamId)?.let { baseClassState ->
                super.onRestoreInstanceState(baseClassState)
            }

            restoreEditorState(bundle)
        }
        ?: super.onRestoreInstanceState(state)
    }

    private fun restoreEditorState(bundle: Bundle) {
        bundle.getString(HtmlFileToRestoreStateParamId)?.let { restoreStateFilePath ->
            val stateFile = File(restoreStateFilePath)

            val htmlToRestore = stateFile.readText()

            val baseUrl = bundle.getString(BaseUrlToRestoreStateParamId)

            setHtml(htmlToRestore, baseUrl)
        }
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        context.asActivity()?.let { activity ->
            keyboardState.init(activity)
        }
    }

    override fun onDetachedFromWindow() {
        keyboardState.cleanUp() // to avoid memory leaks

        super.onDetachedFromWindow()
    }

    val isKeyboardVisible: Boolean
        get() = keyboardState.isKeyboardVisible


    override fun enterEditingMode() {
        super.enterEditingMode()

        javaScriptExecutor.startEditing()

        javaScriptExecutor.makeImagesResizeable()
    }

    override fun enterViewingMode() {
        super.enterViewingMode()

        javaScriptExecutor.disableImageResizing()
    }


    open val baseUrl: String?
        get() = javaScriptExecutor.baseUrl

    /**
     * Returns the last cached editor's html.
     * Usually this is the up to date html. But in case user uses swipe input, some swipe keyboards (especially Samsung's) or pasting text on Samsung devices doesn't fire text changed event,
     * so we're not notified of last entered word. In this case use getCurrentHtmlAsync() to ensure to retrieve current html.
     */
    open fun getCachedHtml(): String {
        return javaScriptExecutor.getCachedHtml()
    }

    @JvmOverloads
    open fun setHtml(html: String, baseUrl: String? = null) {
        javaScriptExecutor.setHtml(html, baseUrl)
    }

    /**
     * This is in most cases the method you want.
     * Queries underlying JavaScript code for real current html (not cached one as getCachedHtml()).
     * Due to the nature of underlying JavaScript implementation this method has to be asynchronous.
     *
     * See getCachedHtml() for explanation why it's sensible to use this method.
     *
     * Convenience method for Kotlin users.
     */
    open fun getCurrentHtmlAsync(callback: (String) -> Unit) {
        javaScriptExecutor.getCurrentHtmlAsync(callback)
    }

    /**
     * This is in most cases the method you want.
     * Queries underlying JavaScript code for real current html (not cached one as getCachedHtml()).
     * Due to the nature of underlying JavaScript implementation this method has to be asynchronous.
     *
     * See getCachedHtml() for explanation why it's sensible to use this method.
     */
    open fun getCurrentHtmlAsync(callback: GetCurrentHtmlCallback) {
        javaScriptExecutor.getCurrentHtmlAsync(callback)
    }

    /**
     * Blocks while doing async getCurrentHtmlAsync() call. You shouldn't call this on UI thread as till response from JavaScript is retrieved UI thread is blocked.
     *
     * Caution: Does not work if called from Android's UI thread!
     * As JavaScript result callback will asynchronously be posted to UI thread. So while UI thread is being block, JavaScript result callback cannot be executed on UI thread.
     */
    open fun getCurrentHtmlBlockingDoesNotWorkOnUiThread(): String {
        return javaScriptExecutor.getCurrentHtmlBlocking()
    }

    /**
     * Returns if html is equal to html RichTextEditor sets by default at start (<p>​</p>)
     * so that RichTextEditor can be considered as 'empty'.
     */
    open fun isDefaultRichTextEditorHtml(html: String): Boolean {
        return javaScriptExecutor.isDefaultRichTextEditorHtml(html)
    }


    /*      Editor base settings        */

    open fun setTheme(theme: Theme) {
        javaScriptExecutor.setTheme(theme)
    }

    open fun setTheme(themeName: String) {
        javaScriptExecutor.setTheme(themeName)
    }

    open fun setEditorFontColor(color: Int) {
        val hex = convertHexColorString(color)
        executeEditorJavaScriptFunction("setBaseTextColor('$hex');")
    }

    open fun setEditorFontFamily(fontFamily: String) {
        this.settings.standardFontFamily = fontFamily
        executeEditorJavaScriptFunction("setBaseFontFamily('$fontFamily');")
    }

    open fun setEditorFontSize(px: Int) {
        this.settings.defaultFontSize = px
        executeEditorJavaScriptFunction("setBaseFontSize('${px}px');")
    }

    open fun setPadding(padding: Int) {
        setPadding(padding, padding, padding, padding)
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        if(isLoaded) {
            context.asActivity()?.runOnUiThread {
                super.setPadding(left, top, right, bottom)
                executeEditorJavaScriptFunction("setPadding('${left}px', '${top}px', '${right}px', '${bottom}px');")
            }

        }
        else { // on older devices setPadding() is called in parent class' constructor -> webView is not set yet
            // don't know why but setPadding() is called multiple times, last times with all values set to 0 and therefore overwriting correct values
            if(paddingToSetOnStart == null || (paddingToSetOnStart?.left == 0 && paddingToSetOnStart?.top == 0 && paddingToSetOnStart?.right == 0 && paddingToSetOnStart?.bottom == 0)) {
                paddingToSetOnStart = Rect(left, top, right, bottom)
            }
        }
    }

    override fun setPaddingRelative(start: Int, top: Int, end: Int, bottom: Int) {
        // still not support RTL.
        setPadding(start, top, end, bottom)
    }

    open fun setEditorBackgroundColor(color: Int) {
        setBackgroundColor(color)
    }

    override fun setBackgroundResource(resid: Int) {
        val bitmap = Utils.decodeResource(context, resid)

        bitmap?.let { // Utils.decodeResource() may returns null
            setBackground(bitmap)
        }
    }

    override fun setBackground(background: Drawable) {
        val bitmap = Utils.toBitmap(background)
        setBackground(bitmap)
    }

    protected open fun setBackground(bitmap: Bitmap) {
        val base64 = Utils.toBase64(bitmap)
        bitmap.recycle()

        executeEditorJavaScriptFunction("setBackgroundImage('url(data:image/png;base64,$base64)');")
    }

    open fun setBackground(url: String) {
        executeEditorJavaScriptFunction("setBackgroundImage('url($url)');")
    }

    open fun setEditorWidth(px: Int) {
        executeEditorJavaScriptFunction("setWidth('" + px + "px');")
    }

    open fun setEditorHeight(px: Int) {
        executeEditorJavaScriptFunction("setHeight('" + px + "px');")
    }

    /**
     * Does actually not work for me
     */
    open fun setPlaceholder(placeholder: String) {
        executeEditorJavaScriptFunction("setPlaceholder('$placeholder');")
    }

    open fun setInputEnabled(inputEnabled: Boolean) {
        executeEditorJavaScriptFunction("setInputEnabled($inputEnabled)")
    }

    open fun loadCSS(cssFile: String) {
        val jsCSSImport = "(function() {" +
                "    var head  = document.getElementsByTagName(\"head\")[0];" +
                "    var link  = document.createElement(\"link\");" +
                "    link.rel  = \"stylesheet\";" +
                "    link.type = \"text/css\";" +
                "    link.href = \"" + cssFile + "\";" +
                "    link.media = \"all\";" +
                "    head.appendChild(link);" +
                "}) ();"
        javaScriptExecutor.executeJavaScript(jsCSSImport)
    }


    open fun addDidHtmlChangeListener(listener: (Boolean) -> Unit) {
        javaScriptExecutor.addDidHtmlChangeListener(listener)
    }

    open fun addDidHtmlChangeListener(listener: DidHtmlChangeListener) {
        javaScriptExecutor.addDidHtmlChangeListener(listener)
    }

    open fun addEditorLoadedListener(listener: EditorLoadedListener) {
        addEditorLoadedListener { listener.editorLoaded() }
    }

    open fun addEditorLoadedListener(listener: () -> Unit) {
        if (isLoaded) {
            listener()
        }
        else {
            editorLoadedListeners.add(listener)
        }
    }

    protected open fun callEditorLoadedListeners() {
        editorLoadedListeners.forEach { listener ->
            try {
                listener()
            } catch (e: Exception) {
                log.error("Could not call EditorLoadedListener $listener", e)
            }
        }

        editorLoadedListeners.clear()
    }


    open fun addOnTouchListener(listener: (MotionEvent) -> Unit) {
        onTouchListeners.add(listener)
    }

    open fun removeOnTouchListener(listener: (MotionEvent) -> Unit) {
        onTouchListeners.remove(listener)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val result = super.dispatchTouchEvent(event)

        ArrayList(onTouchListeners).forEach { listener -> // make a copy of listeners otherwise ConcurrentModificationException will be thrown
            listener(event) }

        return result
    }


    @JvmOverloads
    open fun focusEditor(alsoCallJavaScriptFocusFunction: Boolean = true) {
        this.requestFocus()

        if(alsoCallJavaScriptFocusFunction) { // Calling focus() changes editor's state, this is not desirable in all circumstances
            executeEditorJavaScriptFunction("focus()")
        }
    }

    @JvmOverloads
    open fun focusEditorAndShowKeyboard(alsoCallJavaScriptFocusFunction: Boolean = true) {
        focusEditor(alsoCallJavaScriptFocusFunction)

        this.showKeyboard()
    }

    /**
     * At start up we have to wait some time till editor is ready to be focused
     */
    @JvmOverloads
    open fun focusEditorAndShowKeyboardDelayed(delayMillis: Long = 250, alsoCallJavaScriptFocusFunction: Boolean = true) {
        postDelayed({
            focusEditorAndShowKeyboard(alsoCallJavaScriptFocusFunction)
        }, delayMillis)
    }

    override fun clearFocus() {
        super.clearFocus()

        executeEditorJavaScriptFunction("blurFocus()")
    }

    protected open fun convertHexColorString(color: Int): String {
        val alpha = Color.alpha(color)

        if(alpha == 255) { // without alpha
            return String.format("#%06X", 0xFFFFFF and color)
        }
        else {
            return "rgba(${Color.red(color)}, ${Color.green(color)}, ${Color.blue(color)}, $alpha)"
        }
    }


    protected open fun executeEditorJavaScriptFunction(javaScript: String, resultCallback: ((String) -> Unit)? = null) {
        javaScriptExecutor.executeEditorJavaScriptFunction(javaScript, resultCallback)
    }


}