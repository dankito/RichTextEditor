package net.dankito.richtexteditor.android

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.RelativeLayout
import net.dankito.richtexteditor.android.extensions.showKeyboard
import net.dankito.richtexteditor.android.util.KeyboardState
import org.slf4j.LoggerFactory


class RichTextEditor : RelativeLayout {

    companion object {
        private val log = LoggerFactory.getLogger(RichTextEditor::class.java)
    }


    constructor(context: Context) : super(context) { initEditor(context, null) }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) { initEditor(context, attrs) }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { initEditor(context, attrs) }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) { initEditor(context, attrs) }


    val webView = WebView(context)

    lateinit var javaScriptExecutor: AndroidJavaScriptExecutor
        private set


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

        webView.settings.defaultTextEncodingName = "UTF-8" // otherwise non ASCII text doesn't get displayed correctly
        webView.settings.domStorageEnabled = true // otherwise images won't load
        webView.settings.loadsImagesAutomatically = true
        webView.settings.setSupportZoom(true)
        webView.settings.builtInZoomControls = true
        webView.settings.displayZoomControls = false

        javaScriptExecutor = AndroidJavaScriptExecutor(webView)

        javaScriptExecutor.addLoadedListener {
            (context as? Activity)?.runOnUiThread {
                setEditorFontFamily("serif")
            }
        }
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

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        KeyboardState.init(context as Activity)
    }


    /**
     * Returns the last cached editor's html.
     * Usually this is the up to date html. But in case user uses swipe input, some swipe keyboards (especially Samsung's) or pasting text on Samsung devices doesn't fire text changed event,
     * so we're not notified of last entered word. In this case use retrieveCurrentHtmlAsync() to ensure to retrieve current html.
     */
    fun getHtml(): String {
        return javaScriptExecutor.getHtml()
    }

    @JvmOverloads
    fun setHtml(html: String, baseUrl: String? = null) {
        javaScriptExecutor.setHtml(html, baseUrl)
    }

    /**
     * Queries underlying JavaScript code for latest html.
     * See getHtml() for explanation when it's sensible to call this method.
     */
    fun retrieveCurrentHtmlAsync(callback: (String) -> Unit) {
        javaScriptExecutor.retrieveCurrentHtmlAsync(callback)
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
        javaScriptExecutor.addLoadedListener { // on older devices setPadding() is called in parent class' constructor -> webView is not set yet
//            // don't know why but setPadding() is called multiple times, last times with all values set to 0 and therefore overwriting correct values
//            if(paddingToSetOnStart == null || (paddingToSetOnStart?.left == 0 && paddingToSetOnStart?.top == 0 && paddingToSetOnStart?.right == 0 && paddingToSetOnStart?.bottom == 0)) {

            (context as? Activity)?.runOnUiThread {
                webView.setPadding(left, top, right, bottom)
                executeEditorJavaScriptFunction("setPadding('${left}px', '${top}px', '${right}px', '${bottom}px');")
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
        javaScriptExecutor.executeJavaScript(jsCSSImport)
    }

    @JvmOverloads
    fun focusEditor(alsoCallJavaScriptFocusFunction: Boolean = true) {
        webView.requestFocus()

        if(alsoCallJavaScriptFocusFunction) { // Calling focus() changes editor's state, this is not desirable in all circumstances
            executeEditorJavaScriptFunction("focus()")
        }
    }

    @JvmOverloads
    fun focusEditorAndShowKeyboard(alsoCallJavaScriptFocusFunction: Boolean = true) {
        focusEditor(alsoCallJavaScriptFocusFunction)

        webView.showKeyboard()
    }

    /**
     * At start up we have to wait some time till editor is ready to be focused
     */
    @JvmOverloads
    fun focusEditorAndShowKeyboardDelayed(delayMillis: Long = 250, alsoCallJavaScriptFocusFunction: Boolean = true) {
        postDelayed({
            focusEditorAndShowKeyboard(alsoCallJavaScriptFocusFunction)
        }, delayMillis)
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


    private fun executeEditorJavaScriptFunction(javaScript: String, resultCallback: ((String) -> Unit)? = null) {
        javaScriptExecutor.executeEditorJavaScriptFunction(javaScript, resultCallback)
    }


}