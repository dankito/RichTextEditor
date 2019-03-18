package net.dankito.richtexteditor.android.command

import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.android.toolbar.SelectValueView
import net.dankito.richtexteditor.command.CommandName
import net.dankito.richtexteditor.command.ToolbarCommandStyle
import net.dankito.utils.android.ui.view.IHandlesBackButtonPress


abstract class SelectValueCommand(command: CommandName, icon: ImageReference, style: ToolbarCommandStyle = ToolbarCommandStyle(), commandExecutedListener: (() -> Unit)? = null)
    : SelectValueCommandBase(command, icon, style, commandExecutedListener), IHandlesBackButtonPress {


    private var selectValueView: SelectValueView? = null


    override fun executeCommand(executor: JavaScriptExecutorBase) {
        getSelectValueView(executor)?.toggleShowView()
    }

    private fun getSelectValueView(executor: JavaScriptExecutorBase): SelectValueView? {
        selectValueView?.let { return it }

        editor?.let { editor ->
            val view = SelectValueView(editor.context)
            view.initialize(editor, this, getValuesDisplayTexts()) { position ->
                valueSelected(executor, position)
            }

            this.selectValueView = view
            return view
        }

        return null
    }


    override fun handlesBackButtonPress(): Boolean {
        selectValueView?.let {
            return it.handlesBackButtonPress()
        }

        return false
    }

}