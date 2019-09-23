package net.dankito.richtexteditor.java.fx

import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.scene.web.WebView
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.callback.GetCurrentHtmlCallback
import tornadofx.*


open class RichTextEditor : VBox() {

    companion object {
        const val DefaultHtml = JavaScriptExecutorBase.DefaultHtml
    }


    protected var webView = WebView()

    val javaScriptExecutor = JavaFXJavaScriptExecutor(webView)


    init {
        setupHtmlEditor()
    }



    protected open fun setupHtmlEditor() {
        minHeight = 200.0
        prefHeight = Region.USE_COMPUTED_SIZE
        useMaxWidth = true
        isFillWidth = true

        webView.minHeight = 200.0
        webView.prefHeight = Region.USE_COMPUTED_SIZE
        webView.maxHeight = Double.MAX_VALUE

        webView.isContextMenuEnabled = false
//        webView.onContextMenuRequested = EventHandler<ContextMenuEvent> { event ->
//            // don't why these insets are needed, figured them out by trial an error
//            showContextMenuAtPosition(event.x.toInt() - 14, event.y.toInt() - 12)
//        }

        this.children.add(webView)
        VBox.setVgrow(webView, Priority.ALWAYS)
        webView.prefHeightProperty().bind(this.heightProperty())
        webView.prefWidthProperty().bind(this.widthProperty())
    }


    open fun cleanUp() {
        webView.engine.loadWorker.cancel()

        webView.engine.load("") // to stop audio / video playback etc.
    }



    @JvmOverloads
    open fun focusEditor(alsoCallJavaScriptFocusFunction: Boolean = true) {
        webView.requestFocus()

        if(alsoCallJavaScriptFocusFunction) { // Calling focus() changes editor's state, this is not desirable in all circumstances
            executeEditorJavaScriptFunction("focus()")
        }
    }


    /*      Editor base settings        */

    open fun setEditorFontFamily(fontFamily: String) {
        executeEditorJavaScriptFunction("setBaseFontFamily('$fontFamily');")
    }

    open fun setEditorFontSize(px: Int) {
        executeEditorJavaScriptFunction("setBaseFontSize('${px}px');")
    }

    open fun setPadding(padding: Double) {
        setPadding(padding, padding, padding, padding)
    }

    open fun setPadding(left: Double, top: Double, right: Double, bottom: Double) {
        executeEditorJavaScriptFunction("setPadding('${left}px', '${top}px', '${right}px', '${bottom}px');")
    }

    protected open fun executeEditorJavaScriptFunction(javaScript: String, resultCallback: ((String) -> Unit)? = null) {
        javaScriptExecutor.executeEditorJavaScriptFunction(javaScript, resultCallback)
    }


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
        getCurrentHtmlAsync(object : GetCurrentHtmlCallback {

            override fun htmlRetrieved(html: String) {
                callback(html)
            }

        })
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
     * Returns if html is equal to html RichTextEditor sets by default at start (<p>​</p>)
     * so that RichTextEditor can be considered as 'empty'.
     */
    open fun isDefaultRichTextEditorHtml(html: String): Boolean {
        return javaScriptExecutor.isDefaultRichTextEditorHtml(html)
    }

}
