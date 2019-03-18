package net.dankito.richtexteditor.command

import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.JavaScriptExecutorBase


abstract class InsertCheckboxCommandBase(icon: ImageReference) : ToolbarCommand(CommandName.INSERTCHECKBOX, icon) {


    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.insertCheckbox("")
    }

}