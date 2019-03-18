package net.dankito.richtexteditor.command

import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.JavaScriptExecutorBase


abstract class BoldCommandBase(icon: ImageReference) : ActiveStateToolbarCommand(CommandName.BOLD, icon) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.setBold()
    }

}