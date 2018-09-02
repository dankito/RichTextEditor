package net.dankito.richtexteditor.android.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import net.dankito.richtexteditor.android.command.TextMarkerCommand
import net.dankito.richtexteditor.android.command.ToggleGroupedInsertCommandsCommand
import net.dankito.richtexteditor.android.command.ToggleGroupedTextStylesCommand
import net.dankito.richtexteditor.android.command.UndoCommand
import net.dankito.richtexteditor.command.ToolbarCommand
import net.dankito.utils.android.ui.view.IHandlesTouch


open class GroupedCommandsEditorToolbar : EditorToolbar, IHandlesTouch {

    constructor(context: Context) : super(context) { initToolbar() }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) { initToolbar() }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { initToolbar() }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) { initToolbar() }


    protected val commandsHandlingOutsideTouches = ArrayList<IHandlesTouch>()


    protected open fun initToolbar() {
        addCommand(ToggleGroupedTextStylesCommand())

        addCommand(TextMarkerCommand())

        addCommand(UndoCommand())

        addCommand(ToggleGroupedInsertCommandsCommand())

        addSearchView()
    }


    override fun addCommand(command: ToolbarCommand) {
        super.addCommand(command)

        if(command is IHandlesTouch) {
            commandsHandlingOutsideTouches.add(command)
        }
    }

    override fun handlesTouch(event: MotionEvent): Boolean {
        commandsHandlingOutsideTouches.forEach { commandClosingOnOutsideTouch ->
            if(commandClosingOnOutsideTouch.handlesTouch(event)) {
                return true
            }
        }

        return false
    }

}