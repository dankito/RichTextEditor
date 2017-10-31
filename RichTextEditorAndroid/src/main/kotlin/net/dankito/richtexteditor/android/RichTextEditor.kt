package net.dankito.richtexteditor.android

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Looper
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import net.dankito.richtexteditor.android.command.Commands
import org.slf4j.LoggerFactory
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread


class RichTextEditor : WebView {

    companion object {
        private const val EditorHtmlPath = "file:///android_asset/editor.html"

        private const val TextChangedCallbackScheme = "text-changed-callback://"

        private const val CommandStatesChangedCallbackScheme = "command-states-changed-callback://"

        private val log = LoggerFactory.getLogger(RichTextEditor::class.java)
    }


    constructor(context: Context) : super(context) { initEditor(context, null) }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) { initEditor(context, attrs) }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { initEditor(context, attrs) }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) { initEditor(context, attrs) }


    private var html: String = ""

    private var isLoaded = false

    private val loadedListeners = mutableSetOf<() -> Unit>()

    private var enabledCommands: List<Commands> = ArrayList()

    private val commandStatesUpdatedListeners = mutableSetOf<(enabledCommands: List<Commands>) -> Unit>()


    @SuppressLint("SetJavaScriptEnabled")
    private fun initEditor(context: Context, attributes: AttributeSet?) {
        attributes?.let { applyAttributes(context, it) }

        isVerticalScrollBarEnabled = false
        isHorizontalScrollBarEnabled = false
        settings.javaScriptEnabled = true

        webChromeClient = WebChromeClient()
        webViewClient = editorWebViewClient

        loadUrl(EditorHtmlPath)
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


    fun setHtml(html: String) {
        try {
            executeEditorJavaScriptFunction("setHtml('" + URLEncoder.encode(html, "UTF-8") + "');")

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

    fun setFontSize(fontSize: Int) {
        if (fontSize < 1 || fontSize > 7) {
            log.warn("Font size should have a value between 1-7")
        }
        executeEditorJavaScriptFunction("setFontSize('$fontSize')")
    }

    fun setHeading(heading: Int) {
        executeEditorJavaScriptFunction("setHeading('$heading')")
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


    /*      Editor base settings        */

    fun setEditorFontColor(color: Int) {
        val hex = convertHexColorString(color)
        executeEditorJavaScriptFunction("setBaseTextColor('$hex');")
    }

    fun setEditorFontFamily(fontFamily: String) {
        executeEditorJavaScriptFunction("setBaseFontFamily('$fontFamily');")
    }

    fun setEditorFontSize(px: Int) {
        executeEditorJavaScriptFunction("setBaseFontSize('${px}px');")
    }

    fun setPadding(padding: Int) {
        setPadding(padding, padding, padding, padding)
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)
        executeEditorJavaScriptFunction("setPadding('${left}px', '${top}px', '${right}px', '${bottom}px');")
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

    private fun convertHexColorString(color: Int): String {
        return String.format("#%06X", 0xFFFFFF and color)
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
        evaluateJavascript(javaScript, null)
    }

    private fun executeScriptOnUiThreadForAndroidPre19(javaScript: String) {
        loadUrl("javascript:" + javaScript)
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

        if(TextUtils.indexOf(url, TextChangedCallbackScheme) == 0) {
            textChanged(decodedUrl.substring(TextChangedCallbackScheme.length))
            return true
        }
        else if(TextUtils.indexOf(url, CommandStatesChangedCallbackScheme) == 0) {
            commandStatesChanged(decodedUrl.substring(CommandStatesChangedCallbackScheme.length))
            return true
        }

        return false
    }

    private fun textChanged(html: String) {
        this.html = html
        log.info("Text changed to $html")
    }

    private fun commandStatesChanged(commandStates: String) {
        val states = commandStates.toUpperCase(Locale.ENGLISH)
        val enabledCommands = ArrayList<Commands>()

        Commands.values().forEach { command ->
            if(TextUtils.indexOf(states, command.name) != -1) {
                enabledCommands.add(command)
            }
        }

        this.enabledCommands = enabledCommands
        commandStatesUpdatedListeners.forEach { it.invoke(enabledCommands) }
    }

    fun addCommandStatesUpdatedListener(listener: (enabledCommands: List<Commands>) -> Unit) {
        commandStatesUpdatedListeners.add(listener)

        listener.invoke(this.enabledCommands)
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

        for(listener in HashSet<() -> Unit>(loadedListeners)) {
            thread { callInitializationListener(listener) }
        }

        loadedListeners.clear()
    }

    private fun callInitializationListener(listener: () -> Unit) {
        listener()
    }


}