package net.dankito.richtexteditor.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase


abstract class InsertCheckboxCommandBase(icon: Icon) : ToolbarCommand(CommandName.INSERTCHECKBOX, icon) {


    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.insertCheckbox("")
    }

}