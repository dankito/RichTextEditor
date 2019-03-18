package net.dankito.richtexteditor.command

import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.JavaScriptExecutorBase


abstract class IncreaseIndentCommandBase(icon: ImageReference) : ToolbarCommand(CommandName.INDENT, icon) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.setIndent()
    }

}