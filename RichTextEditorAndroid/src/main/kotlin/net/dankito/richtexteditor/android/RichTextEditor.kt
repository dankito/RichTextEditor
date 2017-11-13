package net.dankito.richtexteditor.android

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Looper
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.RelativeLayout
import com.fasterxml.jackson.databind.ObjectMapper
import net.dankito.richtexteditor.android.command.Command
import net.dankito.richtexteditor.android.command.CommandState
import net.dankito.richtexteditor.android.extensions.showKeyboard
import org.slf4j.LoggerFactory
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.*
import kotlin.concurrent.thread


class RichTextEditor : RelativeLayout {

    companion object {
        private const val EditorHtmlPath = "file:///android_asset/editor.html"

        private const val EditorStateChangedCallbackScheme = "editor-state-changed-callback://"

        private val log = LoggerFactory.getLogger(RichTextEditor::class.java)
    }


    constructor(context: Context) : super(context) { initEditor(context, null) }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) { initEditor(context, attrs) }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { initEditor(context, attrs) }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) { initEditor(context, attrs) }


    private val webView = WebView(context)

    private var html: String = ""

    private var paddingToSetOnStart: Rect? = null

    private var isLoaded = false

    private val loadedListeners = mutableSetOf<() -> Unit>()

    private val objectMapper = ObjectMapper()

    private var commandStates: Map<Command, CommandState> = mapOf()

    private val commandStatesChangedListeners = mutableSetOf<(Map<Command, CommandState>) -> Unit>()

    private var didHtmlChange = false

    private val didHtmlChangeListeners = mutableSetOf<(Boolean) -> Unit>()

    private val htmlChangedListeners = mutableSetOf<(String) -> Unit>()


    @SuppressLint("SetJavaScriptEnabled")
    private fun initEditor(context: Context, attributes: AttributeSet?) {
        val layoutParams = RelativeLayout.LayoutParams(context, attributes)
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        addView(webView, layoutParams)

        attributes?.let { applyAttributes(context, it) }

        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false
        webView.settings.javaScriptEnabled = true

        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = editorWebViewClient

        webView.settings.defaultTextEncodingName = "UTF-8" // otherwise non ASCII text doesn't get displayed correctly
        webView.settings.domStorageEnabled = true // otherwise images won't load
        webView.settings.loadsImagesAutomatically = true
        webView.settings.setSupportZoom(true)
        webView.settings.builtInZoomControls = true
        webView.settings.displayZoomControls = false

        webView.loadUrl(EditorHtmlPath)
    }

    private fun applyAttributes(context: Context, attrs: AttributeSet) {
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


    fun getHtml(): String {
        return html
    }

    fun setHtml(html: String, baseUrl: String? = null) {
        try {
            executeEditorJavaScriptFunction("setHtml('" + URLEncoder.encode(html, "UTF-8") + "', '$baseUrl');")

            this.html = html
        } catch (e: UnsupportedEncodingException) {
            // No handling
        }
    }


    /*      Text Commands        */

    fun undo() {
        executeEditorJavaScriptFunction("undo()")
    }

    fun redo() {
        executeEditorJavaScriptFunction("redo()")
    }

    fun setBold() {
        executeEditorJavaScriptFunction("setBold()")
    }

    fun setItalic() {
        executeEditorJavaScriptFunction("setItalic()")
    }

    fun setUnderline() {
        executeEditorJavaScriptFunction("setUnderline()")
    }

    fun setSubscript() {
        executeEditorJavaScriptFunction("setSubscript()")
    }

    fun setSuperscript() {
        executeEditorJavaScriptFunction("setSuperscript()")
    }

    fun setStrikeThrough() {
        executeEditorJavaScriptFunction("setStrikeThrough()")
    }

    fun setTextColor(color: Int) {
        val hex = convertHexColorString(color)
        executeEditorJavaScriptFunction("setTextColor('$hex')")
    }

    fun setTextBackgroundColor(color: Int) {
        val hex = convertHexColorString(color)
        executeEditorJavaScriptFunction("setTextBackgroundColor('$hex')")
    }

    fun setFontName(fontName: String) {
        executeEditorJavaScriptFunction("setFontName('$fontName')")
    }

    fun setFontSize(fontSize: Int) {
        if (fontSize < 1 || fontSize > 7) {
            log.warn("Font size should have a value between 1-7")
        }
        executeEditorJavaScriptFunction("setFontSize('$fontSize')")
    }

    fun setHeading(heading: Int) {
        executeEditorJavaScriptFunction("setHeading('$heading')")
    }

    fun setFormattingToParagraph() {
        executeEditorJavaScriptFunction("setFormattingToParagraph()")
    }

    fun setPreformat() {
        executeEditorJavaScriptFunction("setPreformat()")
    }

    fun setBlockQuote() {
        executeEditorJavaScriptFunction("setBlockQuote()")
    }

    fun removeFormat() {
        executeEditorJavaScriptFunction("removeFormat()")
    }

    fun setJustifyLeft() {
        executeEditorJavaScriptFunction("setJustifyLeft()")
    }

    fun setJustifyCenter() {
        executeEditorJavaScriptFunction("setJustifyCenter()")
    }

    fun setJustifyRight() {
        executeEditorJavaScriptFunction("setJustifyRight()")
    }

    fun setJustifyFull() {
        executeEditorJavaScriptFunction("setJustifyFull()")
    }

    fun setIndent() {
        executeEditorJavaScriptFunction("setIndent()")
    }

    fun setOutdent() {
        executeEditorJavaScriptFunction("setOutdent()")
    }

    fun insertBulletList() {
        executeEditorJavaScriptFunction("insertBulletList()")
    }

    fun insertNumberedList() {
        executeEditorJavaScriptFunction("insertNumberedList()")
    }


    /*      Insert element              */

    fun insertLink(url: String, title: String) {
        executeEditorJavaScriptFunction("insertLink('$url', '$title')")
    }

    fun insertImage(url: String, alt: String) {
        executeEditorJavaScriptFunction("insertImage('$url', '$alt')")
    }

    fun insertCheckbox(text: String) {
        executeEditorJavaScriptFunction("insertCheckbox('$text')")
    }

    fun insertHtml(html: String) {
        executeEditorJavaScriptFunction("insertHtml('$html')")
    }


    /*      Editor base settings        */

    fun setEditorFontColor(color: Int) {
        val hex = convertHexColorString(color)
        executeEditorJavaScriptFunction("setBaseTextColor('$hex');")
    }

    fun setEditorFontFamily(fontFamily: String) {
        webView.settings.standardFontFamily = fontFamily
        executeEditorJavaScriptFunction("setBaseFontFamily('$fontFamily');")
    }

    fun setEditorFontSize(px: Int) {
        webView.settings.defaultFontSize = px
        executeEditorJavaScriptFunction("setBaseFontSize('${px}px');")
    }

    fun setPadding(padding: Int) {
        setPadding(padding, padding, padding, padding)
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        if(isLoaded) {
            webView.setPadding(left, top, right, bottom)
            executeEditorJavaScriptFunction("setPadding('${left}px', '${top}px', '${right}px', '${bottom}px');")
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

    fun setEditorBackgroundColor(color: Int) {
        setBackgroundColor(color)
    }

    override fun setBackgroundColor(color: Int) {
        super.setBackgroundColor(color)
        webView.setBackgroundColor(color)
    }

    override fun setBackgroundResource(resid: Int) {
        val bitmap = Utils.decodeResource(context, resid)
        setBackground(bitmap)
    }

    override fun setBackground(background: Drawable) {
        val bitmap = Utils.toBitmap(background)
        setBackground(bitmap)
    }

    private fun setBackground(bitmap: Bitmap) {
        val base64 = Utils.toBase64(bitmap)
        bitmap.recycle()

        executeEditorJavaScriptFunction("setBackgroundImage('url(data:image/png;base64,$base64)');")
    }

    fun setBackground(url: String) {
        executeEditorJavaScriptFunction("setBackgroundImage('url($url)');")
    }

    fun setEditorWidth(px: Int) {
        executeEditorJavaScriptFunction("setWidth('" + px + "px');")
    }

    fun setEditorHeight(px: Int) {
        executeEditorJavaScriptFunction("setHeight('" + px + "px');")
    }

    /**
     * Does actually not work for me
     */
    fun setPlaceholder(placeholder: String) {
        executeEditorJavaScriptFunction("setPlaceholder('$placeholder');")
    }

    fun setInputEnabled(inputEnabled: Boolean) {
        executeEditorJavaScriptFunction("setInputEnabled($inputEnabled)")
    }

    fun loadCSS(cssFile: String) {
        val jsCSSImport = "(function() {" +
                "    var head  = document.getElementsByTagName(\"head\")[0];" +
                "    var link  = document.createElement(\"link\");" +
                "    link.rel  = \"stylesheet\";" +
                "    link.type = \"text/css\";" +
                "    link.href = \"" + cssFile + "\";" +
                "    link.media = \"all\";" +
                "    head.appendChild(link);" +
                "}) ();"
        executeJavaScript(jsCSSImport)
    }

    fun focusEditor() {
        webView.requestFocus()
        executeEditorJavaScriptFunction("focus()")
    }

    fun focusEditorAndShowKeyboard() {
        focusEditor()

        webView.showKeyboard()
    }

    override fun clearFocus() {
        super.clearFocus()

        executeEditorJavaScriptFunction("blurFocus()")
        webView.clearFocus()
    }

    private fun convertHexColorString(color: Int): String {
        val alpha = Color.alpha(color)

        if(alpha == 255) { // without alpha
            return String.format("#%06X", 0xFFFFFF and color)
        }
        else {
            return "rgba(${Color.red(color)}, ${Color.green(color)}, ${Color.blue(color)}, $alpha)"
        }
    }


    private fun executeEditorJavaScriptFunction(javaScript: String) {
        executeJavaScript("editor." + javaScript)
    }

    private fun executeJavaScript(javaScript: String) {
        addLoadedListener {
            executeJavaScriptInLoadedEditor(javaScript)
        }
    }

    private fun executeJavaScriptInLoadedEditor(javaScript: String) {
        if(Looper.myLooper() == Looper.getMainLooper()) {
            executeScriptOnUiThread(javaScript)
        }
        else if(context is Activity) {
            (context as? Activity)?.runOnUiThread { executeScriptOnUiThread(javaScript) }
        }
        else {
            log.error("Trying to execute Script '$javaScript', but activity is null")
        }
    }

    private fun executeScriptOnUiThread(javaScript: String) {
        try {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                executeScriptOnUiThreadForAndroid19AndAbove(javaScript)
            }
            else {
                executeScriptOnUiThreadForAndroidPre19(javaScript)
            }
        } catch (ex: Exception) {
            log.error("Could not evaluate JavaScript " + javaScript, ex)
        }

    }

    @TargetApi(19)
    private fun executeScriptOnUiThreadForAndroid19AndAbove(javaScript: String) {
        // evaluateJavascript() only works on API 19 and newer
        webView.evaluateJavascript(javaScript, null)
    }

    private fun executeScriptOnUiThreadForAndroidPre19(javaScript: String) {
        // webView.loadUrl() hides keyboard -> so if keyboard is shown ...
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val isShowingKeyboard = inputManager.isActive(webView)

        webView.loadUrl("javascript:" + javaScript)

        if(isShowingKeyboard) { // .. re-display it after evaluating JavaScript
            webView.showKeyboard()
            webView.postDelayed({
                webView.showKeyboard()
            }, 100)
        }
    }


    private val editorWebViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView, url: String) {
            if(url == EditorHtmlPath) {
                editorLoaded()
            }

            super.onPageFinished(view, url)
        }

        @TargetApi(Build.VERSION_CODES.N)
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            request?.url?.toString()?.let { url ->
                if(shouldOverrideUrlLoading(url)) {
                    return true
                }
            }

            return super.shouldOverrideUrlLoading(view, request)
        }

        @SuppressWarnings("deprecation")
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if(shouldOverrideUrlLoading(url)) {
                return true
            }

            return super.shouldOverrideUrlLoading(view, url)
        }
    }

    private fun shouldOverrideUrlLoading(url: String): Boolean {
        val decodedUrl: String
        try {
            decodedUrl = URLDecoder.decode(url, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            // No handling
            return false
        }

        if(TextUtils.indexOf(url, EditorStateChangedCallbackScheme) == 0) {
            editorStateChanged(decodedUrl.substring(EditorStateChangedCallbackScheme.length))
            return true
        }

        return false
    }

    private fun editorStateChanged(statesString: String) {
        try {
            val editorState = objectMapper.readValue<EditorState>(statesString, EditorState::class.java)
            this.html = editorState.html

            if(this.didHtmlChange != editorState.didHtmlChange) {
                this.didHtmlChange = editorState.didHtmlChange
                didHtmlChangeListeners.forEach { it(didHtmlChange) }
            }

            htmlChangedListeners.forEach { it.invoke(html) }

            handleRetrievedCommandStates(editorState.commandStates)
        } catch(e: Exception) { log.error("Could not parse command states: $statesString", e) }
    }

    private fun handleRetrievedCommandStates(commandStates: MutableMap<Command, CommandState>) {
        determineDerivedCommandStates(commandStates)

        this.commandStates = commandStates

        commandStatesChangedListeners.forEach { it.invoke(this.commandStates) }
    }

    private fun determineDerivedCommandStates(commandStates: MutableMap<Command, CommandState>) {
        commandStates[Command.FORMATBLOCK]?.let { formatCommandState ->
            commandStates.put(Command.H1, CommandState(formatCommandState.executable, isFormatActivated(formatCommandState, "h1")))
            commandStates.put(Command.H2, CommandState(formatCommandState.executable, isFormatActivated(formatCommandState, "h2")))
            commandStates.put(Command.H3, CommandState(formatCommandState.executable, isFormatActivated(formatCommandState, "h3")))
            commandStates.put(Command.H4, CommandState(formatCommandState.executable, isFormatActivated(formatCommandState, "h4")))
            commandStates.put(Command.H5, CommandState(formatCommandState.executable, isFormatActivated(formatCommandState, "h5")))
            commandStates.put(Command.H6, CommandState(formatCommandState.executable, isFormatActivated(formatCommandState, "h6")))
            commandStates.put(Command.P, CommandState(formatCommandState.executable, isFormatActivated(formatCommandState, "p")))
            commandStates.put(Command.PRE, CommandState(formatCommandState.executable, isFormatActivated(formatCommandState, "pre")))
            commandStates.put(Command.BR, CommandState(formatCommandState.executable, isFormatActivated(formatCommandState, "")))
            commandStates.put(Command.BLOCKQUOTE, CommandState(formatCommandState.executable, isFormatActivated(formatCommandState, "blockquote")))
        }

        commandStates[Command.INSERTHTML]?.let { insertHtmlState ->
            commandStates.put(Command.INSERTLINK, insertHtmlState)
            commandStates.put(Command.INSERTIMAGE, insertHtmlState)
            commandStates.put(Command.INSERTCHECKBOX, insertHtmlState)
        }
    }

    private fun isFormatActivated(formatCommandState: CommandState, format: String): String {
        return (formatCommandState.value == format).toString() // rich_text_editor.js reports boolean values as string, so we also have to convert it to string
    }

    fun addCommandStatesChangedListener(listener: (commandStates: Map<Command, CommandState>) -> Unit) {
        commandStatesChangedListeners.add(listener)

        listener.invoke(commandStates)
    }


    fun addDidHtmlChangeListener(listener: (Boolean) -> Unit) {
        didHtmlChangeListeners.add(listener)
    }

    fun addHtmlChangedListener(listener: (String) -> Unit) {
        htmlChangedListeners.add(listener)
    }


    fun addLoadedListener(listener: () -> Unit) {
        if(isLoaded) {
            callInitializationListener(listener)
        }
        else {
            loadedListeners.add(listener)
        }
    }

    private fun editorLoaded() {
        log.info("RichTextEditor is now loaded")

        isLoaded = true

        paddingToSetOnStart?.let {
            setPadding(it.left, it.top, it.right, it.bottom)
            paddingToSetOnStart = null
        }

        setEditorFontFamily("serif")

        for(listener in HashSet<() -> Unit>(loadedListeners)) {
            thread { callInitializationListener(listener) }
        }

        loadedListeners.clear()
    }

    private fun callInitializationListener(listener: () -> Unit) {
        listener()
    }


}