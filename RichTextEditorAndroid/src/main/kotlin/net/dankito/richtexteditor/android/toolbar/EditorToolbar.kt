package net.dankito.richtexteditor.android.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.ImageButton
import android.widget.LinearLayout
import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.android.AndroidCommandView
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.android.command.ICommandRequiringEditor
import net.dankito.richtexteditor.android.command.SelectValueWithPreviewCommand
import net.dankito.richtexteditor.android.util.IHandlesBackButtonPress
import net.dankito.richtexteditor.command.ToolbarCommand
import net.dankito.richtexteditor.command.ToolbarCommandStyle
import net.dankito.richtexteditor.android.util.StyleApplier
import net.dankito.utils.android.extensions.executeActionAfterMeasuringSize


open class EditorToolbar : HorizontalScrollView, IHandlesBackButtonPress {


    constructor(context: Context) : super(context) { initToolbar(context) }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) { initToolbar(context) }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { initToolbar(context) }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) { initToolbar(context) }


    var editor: RichTextEditor? = null
        set(value) {
            field = value

            setRichTextEditorOnCommands(value)
        }

    private val commandInvokedListeners = ArrayList<(ToolbarCommand) -> Unit>()


    private lateinit var linearLayout: LinearLayout

    private val commands = HashMap<ToolbarCommand, View>()

    private val commandsHandlingBackButton = ArrayList<IHandlesBackButtonPress>()

    private val searchViews = ArrayList<SearchView>()

    private val styleApplier = StyleApplier()

    val commandStyle = ToolbarCommandStyle()


    private fun initToolbar(context: Context) {
        this.isFillViewport = true

        linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.HORIZONTAL

        addView(linearLayout)
    }


    fun addCommand(command: ToolbarCommand) {
        val commandView = if(command is SelectValueWithPreviewCommand) SelectValueWithPreviewView(context) else ImageButton(context)
        commandView.tag = command.command // TODO: this is bad, actually it's only needed for UI tests (don't introduce test code in production code)
        commandView.setOnClickListener { commandInvoked(command) }

        linearLayout.addView(commandView)

        commands.put(command, commandView)

        command.executor = editor?.javaScriptExecutor
        command.commandView = AndroidCommandView(commandView)

        if(command is ICommandRequiringEditor) {
            command.editor = editor
        }

        if(command is IHandlesBackButtonPress) {
            commandsHandlingBackButton.add(command)
        }

        applyCommandStyle(command, commandView)
    }

    private fun applyCommandStyle(command: ToolbarCommand, commandView: View) {
        applyCommandStyle(command.icon, command.style, commandView)

        (command.commandView as? AndroidCommandView)?.setTintColor(command.style.enabledTintColor)
    }

    private fun applyCommandStyle(icon: Icon, style: ToolbarCommandStyle, commandView: View) {
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
        val searchView = SearchView(context)

        linearLayout.addView(searchView)
        searchViews.add(searchView)

        searchView.applyStyle(style)

        searchView.editor = editor

        searchView.searchViewExpandedListener = { isExpanded ->
            if(isExpanded) { // scroll to right by lytSearchControls' width
                searchView.lytSearchControls.executeActionAfterMeasuringSize(true) {
                    smoothScrollBy(searchView.lytSearchControls.width, 0)
                }
            }
            else {
                smoothScrollBy(-1 * searchView.lytSearchControls.width, 0)
            }
        }
    }

    fun addSpace() {
        val spaceDefaultWidth = resources.getDimensionPixelSize(R.dimen.editor_toolbar_default_space_width)

        addSpace(spaceDefaultWidth)
    }

    fun addSpace(width: Int) {
        val spaceView = View(context)

        linearLayout.addView(spaceView, width, 1)
    }


    override fun handlesBackButtonPress(): Boolean {
        commandsHandlingBackButton.forEach { command ->
            if(command.handlesBackButtonPress()) {
                return true
            }
        }

        return false
    }


    private fun setRichTextEditorOnCommands(editor: RichTextEditor?) {
        commands.keys.forEach {
            if(it is ICommandRequiringEditor) { // editor has to be set before executor
                it.editor = editor
            }

            it.executor = editor?.javaScriptExecutor
        }

        searchViews.forEach {
            it.editor = editor
        }
    }


    private fun commandInvoked(command: ToolbarCommand) {
        command.commandInvoked()

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