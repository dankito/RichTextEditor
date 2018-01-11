package net.dankito.richtexteditor.java.fx.toolbar

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import net.dankito.richtexteditor.Color
import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.command.SetColorCommand
import net.dankito.richtexteditor.command.ToolbarCommand
import net.dankito.richtexteditor.command.ToolbarCommandStyle
import net.dankito.richtexteditor.java.fx.JavaFXCommandView
import net.dankito.richtexteditor.java.fx.RichTextEditor
import net.dankito.richtexteditor.java.fx.command.SelectValueCommand
import net.dankito.richtexteditor.java.fx.localization.Localization
import net.dankito.richtexteditor.java.fx.util.StyleApplier
import tornadofx.*


open class EditorToolbar : View() {

    var editor: RichTextEditor? = null
        set(value) {
            field = value

            setRichTextEditorOnCommands(value)
        }

    protected val localization = Localization()

    private val commandInvokedListeners = ArrayList<(ToolbarCommand) -> Unit>()


    private lateinit var contentLayout: HBox

    private val commands = HashMap<ToolbarCommand, Region>()

    private val searchViews = ArrayList<SearchView>()

    private val styleApplier = StyleApplier()

    val commandStyle = ToolbarCommandStyle()


    init {
        commandStyle.heightDp = 22
        commandStyle.widthDp = 22
        commandStyle.isActivatedColor = Color.LightGray
        commandStyle.enabledTintColor = Color.Black
    }


    override val root = scrollpane {
        minHeight = commandStyle.heightDp.toDouble() + 8.0
        maxHeight = minHeight

        vbarPolicy = ScrollPane.ScrollBarPolicy.NEVER

        contentLayout = hbox {
            alignment = Pos.CENTER_LEFT
        }
    }


    fun addGroup(group: CommandGroup) {
        contentLayout.add(group.root)

        group.root.minHeight = commandStyle.heightDp.toDouble()
        group.root.maxHeight = group.root.minHeight
//        group.root.maxHeightProperty().bind(contentLayout.heightProperty().subtract(8.0))
        HBox.setMargin(group.root, Insets(2.0))
    }

    fun addVerticalGroup(group: VerticalCommandGroup, toGroup: CommandGroup? = null) {
        if(toGroup == null) {
            contentLayout.add(group)
        }
        else {
            toGroup.addCommand(group)
        }

        group.minHeight = commandStyle.heightDp.toDouble()
        group.maxHeight = group.minHeight
        HBox.setMargin(group, Insets(2.0))


        group.commandInvoked = { commandInvoked(it) }

        group.items.forEach { item ->
            val dummyView = Label()
            commands.put(item.command, dummyView)

            item.command.executor = editor?.javaScriptExecutor
            item.command.commandView = JavaFXCommandView(dummyView)
        }
    }

    fun addCommand(command: ToolbarCommand, toGroup: CommandGroup? = null) {
        val commandView: Region =
            when(command) {
                is SelectValueCommand -> SelectValueView(command) { commandInvoked(it) }
                is SetColorCommand -> SelectColorCommandView(command) { commandInvoked(it) }
                else -> ActiveStateCommandView(command) { commandInvoked(it) }
            }

        if(toGroup == null) {
            contentLayout.add(commandView)
        }
        else {
            toGroup.addCommand(commandView)
        }

        commands.put(command, commandView)

        command.executor = editor?.javaScriptExecutor
        command.commandView = JavaFXCommandView(commandView)

        command.commandView?.setBackgroundColor(command.style.backgroundColor) // set default background, but don't do it in StyleApplier but JavaFXCommandView to avoid duplicate code
        applyCommandStyle(command, commandView)
    }

    private fun applyCommandStyle(command: ToolbarCommand, commandView: Region) {
        applyCommandStyle(command.icon, command.style, commandView)
    }

    internal fun applyCommandStyle(icon: Icon, style: ToolbarCommandStyle, commandView: Region) {
        mergeStyles(commandStyle, style)

        styleApplier.applyCommandStyle(icon, style, commandView)
    }

    private fun mergeStyles(toolbarCommandStyle: ToolbarCommandStyle, commandStyle: ToolbarCommandStyle) {
        if(commandStyle.backgroundColor == ToolbarCommandStyle.DefaultBackgroundColor) {
            commandStyle.backgroundColor = toolbarCommandStyle.backgroundColor
        }

        if(commandStyle.widthDp == ToolbarCommandStyle.DefaultWidthDp) {
            commandStyle.widthDp = toolbarCommandStyle.widthDp
        }

        if(commandStyle.heightDp == ToolbarCommandStyle.DefaultHeightDp) {
            commandStyle.heightDp = toolbarCommandStyle.heightDp
        }

        if(commandStyle.marginRightDp == ToolbarCommandStyle.DefaultMarginRightDp) {
            commandStyle.marginRightDp = toolbarCommandStyle.marginRightDp
        }

        if(commandStyle.paddingDp == ToolbarCommandStyle.DefaultPaddingDp) {
            commandStyle.paddingDp = toolbarCommandStyle.paddingDp
        }

        if(commandStyle.enabledTintColor == ToolbarCommandStyle.DefaultEnabledTintColor) {
            commandStyle.enabledTintColor = toolbarCommandStyle.enabledTintColor
        }

        if(commandStyle.disabledTintColor == ToolbarCommandStyle.DefaultDisabledTintColor) {
            commandStyle.disabledTintColor = toolbarCommandStyle.disabledTintColor
        }

        if(commandStyle.isActivatedColor == ToolbarCommandStyle.DefaultIsActivatedColor) {
            commandStyle.isActivatedColor = toolbarCommandStyle.isActivatedColor
        }
    }


    fun addSearchView(style: SearchViewStyle = SearchViewStyle()) {
        val searchView = SearchView(style)

        contentLayout.add(searchView.root)
        searchViews.add(searchView)

        searchView.executor = editor?.javaScriptExecutor
    }


    private fun setRichTextEditorOnCommands(editor: RichTextEditor?) {
        commands.keys.forEach {
            it.executor = editor?.javaScriptExecutor
        }

        searchViews.forEach {
            it.executor = editor?.javaScriptExecutor
        }
    }


    private fun commandInvoked(command: ToolbarCommand) {
        editor?.focusEditor(false)

        commandInvokedListeners.forEach {
            it.invoke(command)
        }
    }

    fun addCommandInvokedListener(listener: (ToolbarCommand) -> Unit) {
        commandInvokedListeners.add(listener)
    }

    fun removeCommandInvokedListener(listener: (ToolbarCommand) -> Unit) {
        commandInvokedListeners.remove(listener)
    }


}