package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.command.Command

class InsertCheckboxCommand(iconResourceId: Int = R.drawable.ic_insert_checkbox) : ToolbarCommand(Command.INSERTCHECKBOX, iconResourceId) {


    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.insertCheckbox("")
    }

}