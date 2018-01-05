package net.dankito.richtexteditor.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase


abstract class RedoCommandBase(icon: Icon) : ToolbarCommand(CommandName.REDO, icon) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.redo()
    }

}