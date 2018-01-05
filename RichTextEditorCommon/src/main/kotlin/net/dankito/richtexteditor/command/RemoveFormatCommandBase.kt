package net.dankito.richtexteditor.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase


abstract class RemoveFormatCommandBase(icon: Icon) : ToolbarCommand(CommandName.REMOVEFORMAT, icon) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.removeFormat()
    }

}