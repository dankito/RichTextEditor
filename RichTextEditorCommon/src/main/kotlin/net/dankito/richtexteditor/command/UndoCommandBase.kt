package net.dankito.richtexteditor.command

import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.JavaScriptExecutorBase


abstract class UndoCommandBase(icon: ImageReference) : ToolbarCommand(CommandName.UNDO, icon) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.undo()
    }

}