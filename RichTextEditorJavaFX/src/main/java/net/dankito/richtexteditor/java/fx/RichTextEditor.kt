package net.dankito.richtexteditor.java.fx

import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.scene.web.WebView
import tornadofx.*


class RichTextEditor : HBox() {

    private var webView = WebView()

    val javaScriptExecutor = JavaFXJavaScriptExecutor(webView)


    init {
        setupHtmlEditor()
    }



    private fun setupHtmlEditor() {
        minHeight = 200.0
        prefHeight = Region.USE_COMPUTED_SIZE
        useMaxHeight = true
        webView.minHeight = 200.0
        webView.prefHeight = Region.USE_COMPUTED_SIZE
        webView.maxHeight = Double.MAX_VALUE

        webView.isContextMenuEnabled = false
//        webView.onContextMenuRequested = EventHandler<ContextMenuEvent> { event ->
//            // don't why these insets are needed, figured them out by trial an error
//            showContextMenuAtPosition(event.x.toInt() - 14, event.y.toInt() - 12)
//        }

        this.children.add(webView)
        HBox.setHgrow(webView, Priority.ALWAYS)
        webView.prefHeightProperty().bind(this.heightProperty())
        webView.prefWidthProperty().bind(this.widthProperty())

        isFillHeight = true
    }



    @JvmOverloads
    fun focusEditor(alsoCallJavaScriptFocusFunction: Boolean = true) {
        webView.requestFocus()

        if(alsoCallJavaScriptFocusFunction) { // Calling focus() changes editor's state, this is not desirable in all circumstances
            javaScriptExecutor.executeEditorJavaScriptFunction("focus()")
        }
    }


    fun getHtml(): String {
        return javaScriptExecutor.getHtml()
    }

    @JvmOverloads
    fun setHtml(html: String, baseUrl: String? = null) {
        javaScriptExecutor.setHtml(html, baseUrl)
    }

}
