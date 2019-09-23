package net.dankito.richtexteditor

import com.fasterxml.jackson.databind.ObjectMapper
import net.dankito.richtexteditor.callback.DidHtmlChangeListener
import net.dankito.richtexteditor.callback.GetCurrentHtmlCallback
import net.dankito.richtexteditor.callback.LoadedListener
import net.dankito.richtexteditor.command.CommandName
import net.dankito.richtexteditor.command.CommandState
import net.dankito.utils.Color
import org.slf4j.LoggerFactory
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder
import kotlin.concurrent.thread


abstract class JavaScriptExecutorBase {

    companion object {
        const val DefaultHtml = "<p>\u200B</p>"

        const val EditorStateChangedCallbackScheme = "editor-state-changed-callback://"

        const val DefaultEncoding = "UTF-8"

        private val log = LoggerFactory.getLogger(JavaScriptExecutorBase::class.java)
    }


    private var html: String = ""

    private val objectMapper = ObjectMapper()

    private var commandStates: Map<CommandName, CommandState> = mapOf()

    private val commandStatesChangedListeners = mutableSetOf<(Map<CommandName, CommandState>) -> Unit>()

    var didHtmlChange = false
        private set

    private val didHtmlChangeListeners = mutableSetOf<DidHtmlChangeListener>()

    private var isLoaded = false

    private val loadedListeners = mutableSetOf<LoadedListener>()


    abstract fun executeJavaScript(javaScript: String, resultCallback: ((String) -> Unit)? = null)

    fun executeEditorJavaScriptFunction(javaScript: String, resultCallback: ((String) -> Unit)? = null) {
        executeJavaScript("editor." + javaScript, resultCallback)
    }



    /**
     * Returns the last cached editor's html.
     * Usually this is the up to date html. But in case user uses swipe input, some swipe keyboards (especially Samsung's) or pasting text on Samsung devices doesn't fire text changed event,
     * so we're not notified of last entered word. In this case use getCurrentHtmlAsync() to ensure to retrieve current html.
     */
    open fun getCachedHtml(): String {
        return html
    }

    @JvmOverloads
    fun setHtml(html: String, baseUrl: String? = null) {
        try {
            executeEditorJavaScriptFunction("setHtml('" + encodeHtml(html) + "', '$baseUrl');")

            this.html = html
        } catch (e: UnsupportedEncodingException) {
            // No handling
        }
    }

    /**
     * This is in most cases the method you want.
     * Queries underlying JavaScript code for real current html (not cached one as getCachedHtml()).
     * Due to the nature of underlying JavaScript implementation this method has to be asynchronous.
     *
     * See getCachedHtml() for explanation why it's sensible to use this method.
     */
    fun getCurrentHtmlAsync(callback: GetCurrentHtmlCallback) {
        executeEditorJavaScriptFunction("getEncodedHtml()") { html ->
            var decodedHtml = decodeHtml(html)
            if(decodedHtml.startsWith('"') && decodedHtml.endsWith('"')) {
                decodedHtml = decodedHtml.substring(1, decodedHtml.length - 1)
            }

            callback.htmlRetrieved(decodedHtml)
        }
    }

    /**
     * Returns if html is equal to html RichTextEditor sets by default at start (<p>​</p>)
     * so that RichTextEditor can be considered as 'empty'.
     */
    open fun isDefaultRichTextEditorHtml(html: String): Boolean {
        return DefaultHtml == html
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

    fun setTextColor(color: Color) {
        val hex = color.toHexColorString()
        executeEditorJavaScriptFunction("setTextColor('$hex')")
    }

    fun setTextBackgroundColor(color: Color) {
        val hex = color.toHexColorString()
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

    /**
     * The rotation parameter is used to signal that the image is rotated and should be rotated by CSS by given value.
     * Rotation can be one of the following values: 0, 90, 180, 270.
     */
    fun insertImage(url: String, alt: String, rotation: Int = 0) {
        executeEditorJavaScriptFunction("insertImage('$url', '$alt', $rotation)")
    }

    fun insertCheckbox(text: String) {
        executeEditorJavaScriptFunction("insertCheckbox('$text')")
    }

    fun insertHtml(html: String) {
        val encodedHtml = encodeHtml(html)
        executeEditorJavaScriptFunction("insertHtml('$encodedHtml')")
    }


    fun makeImagesResizeable() {
        executeEditorJavaScriptFunction("makeImagesResizeable()")
    }

    fun disableImageResizing() {
        executeEditorJavaScriptFunction("disableImageResizing()")
    }


    protected fun shouldOverrideUrlLoading(url: String): Boolean {
        val decodedUrl: String
        try {
            decodedUrl = decodeHtml(url)
        } catch (e: UnsupportedEncodingException) {
            // No handling
            return false
        }

        if(url.indexOf(EditorStateChangedCallbackScheme) == 0) {
            editorStateChanged(decodedUrl.substring(EditorStateChangedCallbackScheme.length))
            return true
        }

        return false
    }

    private fun editorStateChanged(statesString: String) {
        try {
            val editorState = objectMapper.readValue<EditorState>(statesString, EditorState::class.java)
            this.html = editorState.html

            retrievedEditorState(editorState.didHtmlChange, editorState.commandStates)
        } catch(e: Exception) { log.error("Could not parse command states: $statesString", e) }
    }

    protected fun retrievedEditorState(didHtmlChange: Boolean, commandStates: MutableMap<CommandName, CommandState>) {
        if(this.didHtmlChange != didHtmlChange) {
            this.didHtmlChange = didHtmlChange
            didHtmlChangeListeners.forEach { it.didHtmlChange(didHtmlChange) }
        }

        handleRetrievedCommandStates(commandStates)
    }

    private fun handleRetrievedCommandStates(commandStates: MutableMap<CommandName, CommandState>) {
        determineDerivedCommandStates(commandStates)

        this.commandStates = commandStates

        commandStatesChangedListeners.forEach { it.invoke(this.commandStates) }
    }

    private fun determineDerivedCommandStates(commandStates: MutableMap<CommandName, CommandState>) {
        commandStates[CommandName.FORMATBLOCK]?.let { formatCommandState ->
            commandStates.put(CommandName.H1, CommandState(formatCommandState.executable, isFormatActivated(formatCommandState, "h1")))
            commandStates.put(CommandName.H2, CommandState(formatCommandState.executable, isFormatActivated(formatCommandState, "h2")))
            commandStates.put(CommandName.H3, CommandState(formatCommandState.executable, isFormatActivated(formatCommandState, "h3")))
            commandStates.put(CommandName.H4, CommandState(formatCommandState.executable, isFormatActivated(formatCommandState, "h4")))
            commandStates.put(CommandName.H5, CommandState(formatCommandState.executable, isFormatActivated(formatCommandState, "h5")))
            commandStates.put(CommandName.H6, CommandState(formatCommandState.executable, isFormatActivated(formatCommandState, "h6")))
            commandStates.put(CommandName.P, CommandState(formatCommandState.executable, isFormatActivated(formatCommandState, "p")))
            commandStates.put(CommandName.PRE, CommandState(formatCommandState.executable, isFormatActivated(formatCommandState, "pre")))
            commandStates.put(CommandName.BR, CommandState(formatCommandState.executable, isFormatActivated(formatCommandState, "")))
            commandStates.put(CommandName.BLOCKQUOTE, CommandState(formatCommandState.executable, isFormatActivated(formatCommandState, "blockquote")))
        }

        commandStates[CommandName.INSERTHTML]?.let { insertHtmlState ->
            commandStates.put(CommandName.INSERTLINK, insertHtmlState)
            commandStates.put(CommandName.INSERTIMAGE, insertHtmlState)
            commandStates.put(CommandName.INSERTCHECKBOX, insertHtmlState)
        }
    }

    private fun isFormatActivated(formatCommandState: CommandState, format: String): String {
        return (formatCommandState.value == format).toString() // rich_text_editor.js reports boolean values as string, so we also have to convert it to string
    }

    fun addCommandStatesChangedListener(listener: (commandStates: Map<CommandName, CommandState>) -> Unit) {
        commandStatesChangedListeners.add(listener)

        listener.invoke(commandStates)
    }


    protected fun encodeHtml(html: String, encoding: String = DefaultEncoding): String {
        return URLEncoder.encode(html, encoding)
    }

    protected fun decodeHtml(html: String, encoding: String = DefaultEncoding): String {
        return URLDecoder.decode(html, encoding)
    }


    /**
     * Convenience function for Kotlin users
     */
    fun addDidHtmlChangeListener(listener: (Boolean) -> Unit) {
        addDidHtmlChangeListener(object : DidHtmlChangeListener {

            override fun didHtmlChange(didHtmlChange: Boolean) {
                listener(didHtmlChange)
            }

        })
    }

    fun addDidHtmlChangeListener(listener: DidHtmlChangeListener) {
        didHtmlChangeListeners.add(listener)
    }


    /**
     * Convenience function for Kotlin users
     */
    fun addLoadedListener(listener: () -> Unit) {
        addLoadedListener(object : LoadedListener {

            override fun editorLoaded() {
                listener()
            }

        })
    }

    fun addLoadedListener(listener: LoadedListener) {
        if(isLoaded) {
            callInitializationListener(listener)
        }
        else {
            loadedListeners.add(listener)
        }
    }

    protected open fun editorLoaded() {
        log.info("RichTextEditor is now loaded")

        isLoaded = true

        for(listener in HashSet<LoadedListener>(loadedListeners)) {
            thread { callInitializationListener(listener) }
        }

        loadedListeners.clear()
    }

    private fun callInitializationListener(listener: LoadedListener) {
        listener.editorLoaded()
    }

}