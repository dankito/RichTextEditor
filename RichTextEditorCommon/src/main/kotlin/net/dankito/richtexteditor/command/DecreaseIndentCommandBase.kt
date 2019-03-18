package net.dankito.richtexteditor.command

import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.JavaScriptExecutorBase


abstract class DecreaseIndentCommandBase(icon: ImageReference) : ToolbarCommand(CommandName.OUTDENT, icon) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.setOutdent()
    }

}