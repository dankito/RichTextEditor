package net.dankito.richtexteditor.android

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Looper
import android.view.inputmethod.InputMethodManager
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.android.extensions.showKeyboard
import org.slf4j.LoggerFactory


class AndroidJavaScriptExecutor(private val webView: WebView) : JavaScriptExecutorBase() {

    companion object {
        private const val EditorHtmlPath = "file:///android_asset/editor/editor.html"

        private val log = LoggerFactory.getLogger(AndroidJavaScriptExecutor::class.java)
    }


    private val context = webView.context

    private var javaScriptResultCallback: ((String) -> Unit)? = null


    init {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) { // on pre KitKat Androids there's no other way to get result of a JavaScript function
            webView.addJavascriptInterface(this, "android")
        }

        webView.post { // directly calling webView.loadUrl() results in that editor.html doesn't get loaded -> use post()
            startEditing()

            webView.loadUrl(EditorHtmlPath)
        }
    }


    fun startEditing() {
        webView.webViewClient = editorWebViewClient
    }


    override fun executeJavaScript(javaScript: String, resultCallback: ((String) -> Unit)?) {
        addLoadedListener {
            executeJavaScriptInLoadedEditor(javaScript, resultCallback)
        }
    }

    private fun executeJavaScriptInLoadedEditor(javaScript: String, resultCallback: ((String) -> Unit)? = null) {
        if(Looper.myLooper() == Looper.getMainLooper()) {
            executeScriptOnUiThread(javaScript, resultCallback)
        }
        else if(context is Activity) {
            (context as? Activity)?.runOnUiThread { executeScriptOnUiThread(javaScript, resultCallback) }
        }
        else {
            log.error("Trying to execute Script '$javaScript', but activity is null")
        }
    }

    private fun executeScriptOnUiThread(javaScript: String, resultCallback: ((String) -> Unit)? = null) {
        try {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                executeScriptOnUiThreadForAndroid19AndAbove(javaScript, resultCallback)
            }
            else {
                executeScriptOnUiThreadForAndroidPre19(javaScript, resultCallback)
            }
        } catch (ex: Exception) {
            log.error("Could not evaluate JavaScript " + javaScript, ex)
        }

    }

    @TargetApi(19)
    private fun executeScriptOnUiThreadForAndroid19AndAbove(javaScript: String, resultCallback: ((String) -> Unit)? = null) {
        // evaluateJavascript() only works on API 19 and newer
        webView.evaluateJavascript(javaScript, resultCallback)
    }

    private fun executeScriptOnUiThreadForAndroidPre19(javaScript: String, resultCallback: ((String) -> Unit)? = null) {
        // webView.loadUrl() hides keyboard -> so if keyboard is shown ...
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val isShowingKeyboard = inputManager.isActive(webView)

        if(resultCallback == null) {
            webView.loadUrl("javascript:" + javaScript)
        }
        else {
            this.javaScriptResultCallback = resultCallback
            webView.loadUrl("javascript:android.handleJsResult($javaScript)")
        }

        if(isShowingKeyboard) { // .. re-display it after evaluating JavaScript
            webView.showKeyboard()
            webView.postDelayed({
                webView.showKeyboard()
            }, 100)
        }
    }

    @JavascriptInterface
    fun handleJsResult(result: String) {
        javaScriptResultCallback?.invoke(result)

        javaScriptResultCallback = null
    }


    private val editorWebViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView, url: String) {
            if(url == EditorHtmlPath) {
                editorLoaded()
            }

            super.onPageFinished(view, url)
        }

        @TargetApi(Build.VERSION_CODES.N)
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest?): Boolean {
            request?.url?.toString()?.let { url ->
                if(shouldOverrideUrlLoading(url)) {
                    return true
                }
            }

            return super.shouldOverrideUrlLoading(view, request)
        }

        @Suppress("OverridingDeprecatedMember")
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if(shouldOverrideUrlLoading(url)) {
                return true
            }

            return super.shouldOverrideUrlLoading(view, url)
        }
    }

}