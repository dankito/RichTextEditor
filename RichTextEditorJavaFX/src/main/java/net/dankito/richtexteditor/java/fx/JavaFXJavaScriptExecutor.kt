package net.dankito.richtexteditor.java.fx

import com.sun.webkit.WebPage
import javafx.application.Platform
import javafx.concurrent.Worker
import javafx.scene.web.WebView
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.command.CommandName
import net.dankito.richtexteditor.command.CommandState
import net.dankito.richtexteditor.java.fx.util.HtmlEditorExtractor
import netscape.javascript.JSObject
import org.slf4j.LoggerFactory
import tornadofx.*
import java.io.File
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference


class JavaFXJavaScriptExecutor(private val webView: WebView, private val htmlEditorFolder: File = File("data", "editor")) : JavaScriptExecutorBase() {

    companion object {

        private val commandNames = listOf(CommandName.BOLD, CommandName.ITALIC, CommandName.UNDERLINE, CommandName.STRIKETHROUGH,
                CommandName.SUPERSCRIPT, CommandName.SUBSCRIPT, CommandName.FORMATBLOCK, CommandName.REMOVEFORMAT,
                CommandName.UNDO, CommandName.REDO,
                CommandName.FORECOLOR, CommandName.BACKCOLOR,
                CommandName.FONTNAME, CommandName.FONTSIZE,
                CommandName.JUSTIFYLEFT, CommandName.JUSTIFYCENTER, CommandName.JUSTIFYRIGHT, CommandName.JUSTIFYFULL,
                CommandName.INDENT, CommandName.OUTDENT,
                CommandName.INSERTUNORDEREDLIST, CommandName.INSERTORDEREDLIST, CommandName.INSERTHORIZONTALRULE, CommandName.INSERTHTML)

        private val log = LoggerFactory.getLogger(JavaFXJavaScriptExecutor::class.java)
    }


    private var engine = webView.engine

    var page: WebPage? = null
        private set


    init {
        // we need to extract html editor files as due to a bug in JavaFX WebView local files like images aren't displayed in html files loaded from resource, see https://stackoverflow.com/a/27883471
        HtmlEditorExtractor().extractAsync(htmlEditorFolder) { extractedEditorHtmlFile ->
            extractedEditorHtmlFile?.let {
                runLater { loadEditorHtml(it) }
            }
        }
    }

    private fun loadEditorHtml(htmlEditorFile: File) {
        engine.loadWorker.stateProperty().addListener { _, _, newState ->
            if(newState === Worker.State.SUCCEEDED) {
                editorLoaded()
            }
            else if (newState === Worker.State.FAILED) {
                log.error("Loading RichTextEditor failed: ${engine.loadWorker.message}", engine.loadWorker.exception)
                // TODO: notify user
            }
        }

        try {
            val pageField = engine.javaClass.getDeclaredField("page")
            pageField.isAccessible = true
            this.page = pageField.get(engine) as? WebPage
        } catch(e: Exception) { log.error("Could not access page object", e) }

        setJavaScriptMember("javafx", this)

        engine.load(htmlEditorFile.toURI().toString())
    }


    /**
     * Compared to AndroidJavaScriptExecutor html property is not always up to date with current html -> retrieve
     */
    override fun getHtml(): String {
        val result = AtomicReference<String>()
        val countDownLatch = CountDownLatch(1)

        executeEditorJavaScriptFunction("_getHtml()") {
            result.set(it)
            countDownLatch.countDown()
        }

        try { countDownLatch.await(3, TimeUnit.SECONDS) } catch(ignored: Exception) { }

        return result.get()
    }


    override fun executeJavaScript(javaScript: String, resultCallback: ((String) -> Unit)?) {
        addLoadedListener {
            executeJavaScriptInLoadedEditor(javaScript, resultCallback)
        }
    }

    private fun executeJavaScriptInLoadedEditor(javaScript: String, resultCallback: ((String) -> Unit)? = null) {
        if(Platform.isFxApplicationThread()) {
            executeScriptOnUiThread(javaScript, resultCallback)
        }
        else {
            runLater { executeScriptOnUiThread(javaScript, resultCallback) }
        }
    }

    private fun executeScriptOnUiThread(javaScript: String, resultCallback: ((String) -> Unit)? = null) {
        try {
            val result = engine.executeScript(javaScript)

            resultCallback?.let {
                if(result is String) {
                    resultCallback.invoke(result)
                }
                else if(result is JSObject) {
                    resultCallback.invoke(result.toString())
                }
            }
        } catch (e: Exception) {
            if(e.message == "JavaScript execution terminated.") { // sometimes JavaScript execution fails, then simply re-issue that command and then it's working
                executeScriptOnUiThread(javaScript, resultCallback)
                return
            }

            log.error("Could not execute JavaScript $javaScript", e)
            resultCallback?.invoke("") // TODO: what to return in this case? A NullObject? How to get JavaScript 'undefined' JSObject?
        }
    }



    private fun setJavaScriptMember(name: String, member: Any) {
        try {
            val window = engine.executeScript("window") as? JSObject
            window?.setMember(name, member)
        } catch (e: Exception) {
            log.error("Could not set JavaScript member '$name' to $member", e)
        }
    }


    /*      JavaScript bridge methods       */

    fun updateEditorState(didHtmlChange: Boolean) {
        page?.let { page ->
            val commandStates = HashMap<CommandName, CommandState>()
            commandNames.forEach { name ->
                try {
                    val state = CommandState(page.queryCommandEnabled(name.toString()), page.queryCommandValue(name.toString()) ?: "")
                    commandStates.put(name, state)
                } catch(e: Exception) { log.error("Could not get command state for $name", e) }
            }

            retrievedEditorState(didHtmlChange, commandStates)
        }
    }

}