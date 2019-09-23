package net.dankito.richtexteditor

import com.fasterxml.jackson.databind.ObjectMapper
import net.dankito.richtexteditor.callback.DidHtmlChangeListener
import net.dankito.richtexteditor.callback.GetCurrentHtmlCallback
import net.dankito.richtexteditor.callback.LoadedListener
import net.dankito.richtexteditor.command.CommandName
import net.dankito.richtexteditor.command.CommandState
import net.dankito.richtexteditor.model.Theme
import net.dankito.utils.Color
import org.slf4j.LoggerFactory
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference
import kotlin.concurrent.thread


abstract class JavaScriptExecutorBase {

    companion object {
        const val DefaultHtml = "<p>\u200B</p>"

        const val EditorStateChangedCallbackScheme = "editor-state-changed-callback://"

        const val DefaultEncoding = "UTF-8"

        private val log = LoggerFactory.getLogger(JavaScriptExecutorBase::class.java)
    }


    protected var htmlField: String = ""

    protected var hasFirstStateBeenReceived = false

    var baseUrl: String? = null

    protected val objectMapper = ObjectMapper()

    protected var commandStates: Map<CommandName, CommandState> = mapOf()

    protected val commandStatesChangedListeners = mutableSetOf<(Map<CommandName, CommandState>) -> Unit>()

    var didHtmlChange = false
        protected set

    protected val didHtmlChangeListeners = mutableSetOf<DidHtmlChangeListener>()

    protected var isLoaded = false

    protected val loadedListeners = mutableSetOf<LoadedListener>()


    abstract fun executeJavaScript(javaScript: String, resultCallback: ((String) -> Unit)? = null)

    open fun executeEditorJavaScriptFunction(javaScript: String, resultCallback: ((String) -> Unit)? = null) {
        executeJavaScript("editor." + javaScript, resultCallback)
    }



    /**
     * Returns the last cached editor's html.
     * Usually this is the up to date html. But in case user uses swipe input, some swipe keyboards (especially Samsung's) or pasting text on Samsung devices doesn't fire text changed event,
     * so we're not notified of last entered word. In this case use getCurrentHtmlAsync() to ensure to retrieve current html.
     */
    open fun getCachedHtml(): String {
        return htmlField
    }

    @JvmOverloads
    open fun setHtml(html: String, baseUrl: String? = null) {
        this.baseUrl = baseUrl

        try {
            executeEditorJavaScriptFunction("setHtml('" + encodeHtml(html) + "', '$baseUrl');")

            this.htmlField = html
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
        executeEditorJavaScriptFunction("getEncodedHtml()") { html ->
            var decodedHtml = decodeHtml(html)
            if(decodedHtml.startsWith('"') && decodedHtml.endsWith('"')) {
                decodedHtml = decodedHtml.substring(1, decodedHtml.length - 1)
            }

            callback.htmlRetrieved(decodedHtml)
        }
    }

    /**
     * Blocks while doing async getCurrentHtmlAsync() call. You shouldn't call this on UI thread as till response from JavaScript is retrieved UI thread is blocked.
     *
     * Caution: Does not work if called from Android's UI thread!
     * As JavaScript result callback will asynchronously be posted to UI thread. So while UI thread is being block, JavaScript result callback cannot be executed on UI thread.
     */
    open fun getCurrentHtmlBlocking(): String {
        val result = AtomicReference(htmlField)
        val countDownLatch = CountDownLatch(1)

        getCurrentHtmlAsync {
            result.set(it)
            countDownLatch.countDown()
        }

        try { countDownLatch.await(4, TimeUnit.SECONDS) } catch(e: Exception) { log.info("Couldn't get current html blocking", e) }

        return result.get()
    }

    /**
     * Returns if html is equal to html RichTextEditor sets by default at start (<p>​</p>)
     * so that RichTextEditor can be considered as 'empty'.
     */
    open fun isDefaultRichTextEditorHtml(html: String): Boolean {
        return DefaultHtml == html
    }


    /*      Text Commands        */

    open fun undo() {
        executeEditorJavaScriptFunction("undo()")
    }

    open fun redo() {
        executeEditorJavaScriptFunction("redo()")
    }

    open fun setBold() {
        executeEditorJavaScriptFunction("setBold()")
    }

    open fun setItalic() {
        executeEditorJavaScriptFunction("setItalic()")
    }

    open fun setUnderline() {
        executeEditorJavaScriptFunction("setUnderline()")
    }

    open fun setSubscript() {
        executeEditorJavaScriptFunction("setSubscript()")
    }

    open fun setSuperscript() {
        executeEditorJavaScriptFunction("setSuperscript()")
    }

    open fun setStrikeThrough() {
        executeEditorJavaScriptFunction("setStrikeThrough()")
    }

    open fun setTextColor(color: Color) {
        val hex = color.toHexColorString()
        executeEditorJavaScriptFunction("setTextColor('$hex')")
    }

    open fun setTextBackgroundColor(color: Color) {
        val hex = color.toHexColorString()
        executeEditorJavaScriptFunction("setTextBackgroundColor('$hex')")
    }

    open fun setFontName(fontName: String) {
        executeEditorJavaScriptFunction("setFontName('$fontName')")
    }

    open fun setFontSize(fontSize: Int) {
        if (fontSize < 1 || fontSize > 7) {
            log.warn("Font size should have a value between 1-7")
        }
        executeEditorJavaScriptFunction("setFontSize('$fontSize')")
    }

    open fun setHeading(heading: Int) {
        executeEditorJavaScriptFunction("setHeading('$heading')")
    }

    open fun setFormattingToParagraph() {
        executeEditorJavaScriptFunction("setFormattingToParagraph()")
    }

    open fun setPreformat() {
        executeEditorJavaScriptFunction("setPreformat()")
    }

    open fun setBlockQuote() {
        executeEditorJavaScriptFunction("setBlockQuote()")
    }

    open fun removeFormat() {
        executeEditorJavaScriptFunction("removeFormat()")
    }

    open fun setJustifyLeft() {
        executeEditorJavaScriptFunction("setJustifyLeft()")
    }

    open fun setJustifyCenter() {
        executeEditorJavaScriptFunction("setJustifyCenter()")
    }

    open fun setJustifyRight() {
        executeEditorJavaScriptFunction("setJustifyRight()")
    }

    open fun setJustifyFull() {
        executeEditorJavaScriptFunction("setJustifyFull()")
    }

    open fun setIndent() {
        executeEditorJavaScriptFunction("setIndent()")
    }

    open fun setOutdent() {
        executeEditorJavaScriptFunction("setOutdent()")
    }

    open fun insertBulletList() {
        executeEditorJavaScriptFunction("insertBulletList()")
    }

    open fun insertNumberedList() {
        executeEditorJavaScriptFunction("insertNumberedList()")
    }


    /*      Insert element              */

    open fun insertLink(url: String, title: String) {
        executeEditorJavaScriptFunction("insertLink('$url', '$title')")
    }

    /**
     * The rotation parameter is used to signal that the image is rotated and should be rotated by CSS by given value.
     * Rotation can be one of the following values: 0, 90, 180, 270.
     */
    open fun insertImage(url: String, alt: String, rotation: Int = 0) {
        executeEditorJavaScriptFunction("insertImage('$url', '$alt', $rotation)")
    }

    open fun insertCheckbox(text: String) {
        executeEditorJavaScriptFunction("insertCheckbox('$text')")
    }

    open fun insertHtml(html: String) {
        val encodedHtml = encodeHtml(html)
        executeEditorJavaScriptFunction("insertHtml('$encodedHtml')")
    }


    open fun makeImagesResizeable() {
        executeEditorJavaScriptFunction("makeImagesResizeable()")
    }

    open fun disableImageResizing() {
        executeEditorJavaScriptFunction("disableImageResizing()")
    }


    fun setTheme(theme: Theme) {
        setTheme(theme.themeName)
    }

    fun setTheme(themeName: String) {
        executeEditorJavaScriptFunction("setTheme('$themeName');")
    }


    protected open fun shouldOverrideUrlLoading(url: String): Boolean {
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

    protected open fun editorStateChanged(statesString: String) {
        try {
            val editorState = objectMapper.readValue<EditorState>(statesString, EditorState::class.java)

            if (hasFirstStateBeenReceived) { // when first EditorState is received, its html is still the default value but not that one set by setHtml()
                this.htmlField = editorState.html
            }
            hasFirstStateBeenReceived = true

            retrievedEditorState(editorState.didHtmlChange, editorState.commandStates)
        } catch(e: Exception) { log.error("Could not parse command states: $statesString", e) }
    }

    protected open fun retrievedEditorState(didHtmlChange: Boolean, commandStates: MutableMap<CommandName, CommandState>) {
        if(this.didHtmlChange != didHtmlChange) {
            this.didHtmlChange = didHtmlChange
            didHtmlChangeListeners.forEach { it.didHtmlChange(didHtmlChange) }
        }

        handleRetrievedCommandStates(commandStates)
    }

    protected open fun handleRetrievedCommandStates(commandStates: MutableMap<CommandName, CommandState>) {
        determineDerivedCommandStates(commandStates)

        this.commandStates = commandStates

        commandStatesChangedListeners.forEach { it.invoke(this.commandStates) }
    }

    protected open fun determineDerivedCommandStates(commandStates: MutableMap<CommandName, CommandState>) {
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

    protected open fun isFormatActivated(formatCommandState: CommandState, format: String): String {
        return (formatCommandState.value == format).toString() // rich_text_editor.js reports boolean values as string, so we also have to convert it to string
    }

    open fun addCommandStatesChangedListener(listener: (commandStates: Map<CommandName, CommandState>) -> Unit) {
        commandStatesChangedListeners.add(listener)

        listener.invoke(commandStates)
    }


    protected open fun encodeHtml(html: String, encoding: String = DefaultEncoding): String {
        return URLEncoder.encode(html, encoding)
    }

    protected open fun decodeHtml(html: String, encoding: String = DefaultEncoding): String {
        return URLDecoder.decode(html, encoding)
    }


    /**
     * Convenience function for Kotlin users
     */
    open fun addDidHtmlChangeListener(listener: (Boolean) -> Unit) {
        addDidHtmlChangeListener(object : DidHtmlChangeListener {

            override fun didHtmlChange(didHtmlChange: Boolean) {
                listener(didHtmlChange)
            }

        })
    }

    open fun addDidHtmlChangeListener(listener: DidHtmlChangeListener) {
        didHtmlChangeListeners.add(listener)
    }


    /**
     * Convenience function for Kotlin users
     */
    open fun addLoadedListener(listener: () -> Unit) {
        addLoadedListener(object : LoadedListener {

            override open fun editorLoaded() {
                listener()
            }

        })
    }

    open fun addLoadedListener(listener: LoadedListener) {
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

    protected open fun callInitializationListener(listener: LoadedListener) {
        listener.editorLoaded()
    }

}