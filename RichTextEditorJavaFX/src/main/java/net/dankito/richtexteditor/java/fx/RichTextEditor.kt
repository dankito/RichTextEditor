package net.dankito.richtexteditor.java.fx

import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.scene.web.WebView
import tornadofx.*


open class RichTextEditor : VBox() {

    private var webView = WebView()

    val javaScriptExecutor = JavaFXJavaScriptExecutor(webView)


    init {
        setupHtmlEditor()
    }



    private fun setupHtmlEditor() {
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



    @JvmOverloads
    fun focusEditor(alsoCallJavaScriptFocusFunction: Boolean = true) {
        webView.requestFocus()

        if(alsoCallJavaScriptFocusFunction) { // Calling focus() changes editor's state, this is not desirable in all circumstances
            executeEditorJavaScriptFunction("focus()")
        }
    }


    /*      Editor base settings        */

    fun setEditorFontFamily(fontFamily: String) {
        executeEditorJavaScriptFunction("setBaseFontFamily('$fontFamily');")
    }

    fun setEditorFontSize(px: Int) {
        executeEditorJavaScriptFunction("setBaseFontSize('${px}px');")
    }

    fun setPadding(padding: Double) {
        setPadding(padding, padding, padding, padding)
    }

    fun setPadding(left: Double, top: Double, right: Double, bottom: Double) {
        executeEditorJavaScriptFunction("setPadding('${left}px', '${top}px', '${right}px', '${bottom}px');")
    }

    private fun executeEditorJavaScriptFunction(javaScript: String, resultCallback: ((String) -> Unit)? = null) {
        javaScriptExecutor.executeEditorJavaScriptFunction(javaScript, resultCallback)
    }


    fun getHtml(): String {
        return javaScriptExecutor.getHtml()
    }

    @JvmOverloads
    fun setHtml(html: String, baseUrl: String? = null) {
        javaScriptExecutor.setHtml(html, baseUrl)
    }

}
