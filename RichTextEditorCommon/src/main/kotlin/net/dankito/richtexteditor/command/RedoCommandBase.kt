package net.dankito.richtexteditor.command

import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.JavaScriptExecutorBase


abstract class RedoCommandBase(icon: ImageReference) : ToolbarCommand(CommandName.REDO, icon) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.redo()
    }

}