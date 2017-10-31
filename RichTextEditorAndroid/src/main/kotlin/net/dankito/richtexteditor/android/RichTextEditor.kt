package net.dankito.richtexteditor.android

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Looper
import android.text.TextUtils
import android.util.AttributeSet
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import org.slf4j.LoggerFactory
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder
import kotlin.concurrent.thread


class RichTextEditor : WebView {

    companion object {
        private const val EditorHtmlPath = "file:///android_asset/editor.html"

        private const val TextChangedCallbackScheme = "text-changed-callback://"

        private val log = LoggerFactory.getLogger(RichTextEditor::class.java)
    }


    constructor(context: Context?) : super(context) { initEditor(context) }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { initEditor(context) }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { initEditor(context) }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) { initEditor(context) }


    private var html: String = ""

    private var isLoaded = false

    private val loadedListeners = mutableSetOf<() -> Unit>()


    @SuppressLint("SetJavaScriptEnabled")
    private fun initEditor(context: Context?) {
        isVerticalScrollBarEnabled = false
        isHorizontalScrollBarEnabled = false
        settings.javaScriptEnabled = true

        webChromeClient = WebChromeClient()
        webViewClient = editorWebViewClient

        loadUrl(EditorHtmlPath)
    }


    fun setHtml(html: String) {
        try {
            executeJavaScript("setHtml('" + URLEncoder.encode(html, "UTF-8") + "');")

            this.html = html
        } catch (e: UnsupportedEncodingException) {
            // No handling
        }
    }


    /*      Commands        */

    fun undo() {
        executeJavaScript("undo()")
    }

    fun redo() {
        executeJavaScript("redo()")
    }

    fun setBold() {
        executeJavaScript("setBold()")
    }

    fun setItalic() {
        executeJavaScript("setItalic()")
    }

    fun setUnderline() {
        executeJavaScript("setUnderline()")
    }

    fun setSubscript() {
        executeJavaScript("setSubscript()")
    }

    fun setSuperscript() {
        executeJavaScript("setSuperscript()")
    }

    fun setStrikeThrough() {
        executeJavaScript("setStrikeThrough()")
    }

    fun setBlockquote() {
        executeJavaScript("setBlockquote()")
    }

    fun setJustifyLeft() {
        executeJavaScript("setJustifyLeft()")
    }

    fun setJustifyCenter() {
        executeJavaScript("setJustifyCenter()")
    }

    fun setJustifyRight() {
        executeJavaScript("setJustifyRight()")
    }

    fun setJustifyFull() {
        executeJavaScript("setJustifyFull()")
    }

    fun setIndent() {
        executeJavaScript("setIndent()")
    }

    fun setOutdent() {
        executeJavaScript("setOutdent()")
    }

    fun insertBulletList() {
        executeJavaScript("insertBulletList()")
    }

    fun insertNumberedList() {
        executeJavaScript("insertNumberedList()")
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
        val statement = "editor." + javaScript

        try {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                executeScriptOnUiThreadForAndroid19AndAbove(statement)
            }
            else {
                executeScriptOnUiThreadForAndroidPre19(statement)
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
        val decode: String
        try {
            decode = URLDecoder.decode(url, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            // No handling
            return false
        }


        if (TextUtils.indexOf(url, TextChangedCallbackScheme) == 0) {
            textChanged(decode.substring(TextChangedCallbackScheme.length))
            return true
        }

        return false
    }

    private fun textChanged(html: String) {
        this.html = html
        log.info("Text changed to $html")
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