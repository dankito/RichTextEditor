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
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.RelativeLayout
import com.fasterxml.jackson.databind.ObjectMapper
import net.dankito.richtexteditor.android.command.CommandState
import net.dankito.richtexteditor.android.command.Commands
import org.slf4j.LoggerFactory
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.*
import kotlin.collections.HashMap
import kotlin.concurrent.thread


class RichTextEditor : RelativeLayout {

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


    private val webView = WebView(context)

    private var html: String = ""

    private var isLoaded = false

    private val loadedListeners = mutableSetOf<() -> Unit>()

    private val objectMapper = ObjectMapper()
    private val commandStatesType = objectMapper.typeFactory.constructParametricType(HashMap::class.java, Commands::class.java, CommandState::class.java)

    private var commandStates: Map<Commands, CommandState> = mapOf()

    private val commandStatesChangedListeners = mutableSetOf<(Map<Commands, CommandState>) -> Unit>()


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

    fun setPreformatted() {
        executeEditorJavaScriptFunction("setPreformatted()")
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
        executeEditorJavaScriptFunction("setBaseFontFamily('$fontFamily');")
    }

    fun setEditorFontSize(px: Int) {
        executeEditorJavaScriptFunction("setBaseFontSize('${px}px');")
    }

    fun setPadding(padding: Int) {
        setPadding(padding, padding, padding, padding)
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        webView.setPadding(left, top, right, bottom)
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

    fun clearFocusEditor() {
        executeEditorJavaScriptFunction("blurFocus()")
        webView.clearFocus()
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
        webView.evaluateJavascript(javaScript, null)
    }

    private fun executeScriptOnUiThreadForAndroidPre19(javaScript: String) {
        webView.loadUrl("javascript:" + javaScript)
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
    }

    private fun commandStatesChanged(statesString: String) {
        try {
            val commandStates = objectMapper.readValue<MutableMap<Commands, CommandState>>(statesString, commandStatesType)

            determineDerivedCommandStates(commandStates)

            log.info("Command states are now $commandStates")
            this.commandStates = commandStates
            commandStatesChangedListeners.forEach { it.invoke(this.commandStates) }
        } catch(e: Exception) { log.error("Could not parse command states: $statesString", e) }
    }

    private fun determineDerivedCommandStates(commandStates: MutableMap<Commands, CommandState>) {
        commandStates[Commands.FORMATBLOCK]?.let { formatCommandState ->
            commandStates.put(Commands.H1, CommandState(formatCommandState.executable, isFormatActivated(formatCommandState, "h1")))
            commandStates.put(Commands.H2, CommandState(formatCommandState.executable, isFormatActivated(formatCommandState, "h2")))
            commandStates.put(Commands.H3, CommandState(formatCommandState.executable, isFormatActivated(formatCommandState, "h3")))
            commandStates.put(Commands.H4, CommandState(formatCommandState.executable, isFormatActivated(formatCommandState, "h4")))
            commandStates.put(Commands.H5, CommandState(formatCommandState.executable, isFormatActivated(formatCommandState, "h5")))
            commandStates.put(Commands.H6, CommandState(formatCommandState.executable, isFormatActivated(formatCommandState, "h6")))
            commandStates.put(Commands.P, CommandState(formatCommandState.executable, isFormatActivated(formatCommandState, "p")))
            commandStates.put(Commands.PRE, CommandState(formatCommandState.executable, isFormatActivated(formatCommandState, "pre")))
            commandStates.put(Commands.BR, CommandState(formatCommandState.executable, isFormatActivated(formatCommandState, "")))
            commandStates.put(Commands.BLOCKQUOTE, CommandState(formatCommandState.executable, isFormatActivated(formatCommandState, "blockquote")))
        }

        commandStates[Commands.INSERTHTML]?.let { insertHtmlState ->
            commandStates.put(Commands.INSERTLINK, insertHtmlState)
            commandStates.put(Commands.INSERTIMAGE, insertHtmlState)
            commandStates.put(Commands.INSERTCHECKBOX, insertHtmlState)
        }
    }

    private fun isFormatActivated(formatCommandState: CommandState, format: String): String {
        return (formatCommandState.value == format).toString() // rich_text_editor.js reports boolean values as string, so we also have to convert it to string
    }

    fun addCommandStatesChangedListener(listener: (commandStates: Map<Commands, CommandState>) -> Unit) {
        commandStatesChangedListeners.add(listener)

        listener.invoke(commandStates)
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