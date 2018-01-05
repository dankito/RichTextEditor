package net.dankito.richtexteditor.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase


abstract class UndoCommandBase(icon: Icon) : ToolbarCommand(CommandName.UNDO, icon) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.undo()
    }

}