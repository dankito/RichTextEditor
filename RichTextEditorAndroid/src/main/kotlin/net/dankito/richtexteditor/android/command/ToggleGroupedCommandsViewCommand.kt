package net.dankito.richtexteditor.android.command

import android.view.MotionEvent
import android.view.View
import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.android.extensions.hideView
import net.dankito.richtexteditor.android.toolbar.GroupedCommandsView
import net.dankito.richtexteditor.android.util.IHandlesBackButtonPress
import net.dankito.richtexteditor.android.util.IHandlesTouch
import net.dankito.richtexteditor.command.CommandName
import net.dankito.richtexteditor.command.ToolbarCommand
import net.dankito.richtexteditor.command.ToolbarCommandStyle
import net.dankito.utils.android.extensions.isTouchInsideView


abstract class ToggleGroupedCommandsViewCommand(command: CommandName, icon: Icon, style: ToolbarCommandStyle = ToolbarCommandStyle(), commandExecutedListener: (() -> Unit)? = null)
    : ToolbarCommand(command, icon, style, commandExecutedListener), ICommandRequiringEditor, IHandlesBackButtonPress, IHandlesTouch {

    override var editor: RichTextEditor? = null

    private var groupedCommandsView: GroupedCommandsView? = null


    protected abstract fun createGroupedCommandsView(editor: RichTextEditor): GroupedCommandsView


    override fun executeCommand(executor: JavaScriptExecutorBase) {
        getGroupedCommandsView()?.toggleShowView()
    }

    fun applyStyleToGroupedCommands(style: ToolbarCommandStyle) {
        getGroupedCommandsView()?.applyStyleToGroupedCommands(style)
    }

    private fun getGroupedCommandsView(): GroupedCommandsView? {
        groupedCommandsView?.let { return it }

        editor?.let { editor ->
            val view = createGroupedCommandsView(editor)
            view.initialize(editor, this)

            this.groupedCommandsView = view
            return view
        }

        return null
    }


    override fun handlesBackButtonPress(): Boolean {
        getGroupedCommandsView()?.let { groupedCommandsView ->
            return groupedCommandsView.handlesBackButtonPress()
        }

        return false
    }

    override fun handlesTouch(event: MotionEvent): Boolean {
        getGroupedCommandsView()?.let { groupedCommandsView ->
            if(groupedCommandsView.visibility == View.VISIBLE) {
                if(groupedCommandsView.isTouchInsideView(event) == false) { // a touch outside visible GroupedCommandsView
                    groupedCommandsView.hideView() // -> close it

                    return true
                }
            }
        }

        return false
    }

}