package net.dankito.richtexteditor.command

import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.JavaScriptExecutorBase


abstract class RemoveFormatCommandBase(icon: ImageReference) : ToolbarCommand(CommandName.REMOVEFORMAT, icon) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.removeFormat()
    }

}