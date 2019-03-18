package net.dankito.richtexteditor.command

import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.JavaScriptExecutorBase

abstract class SuperscriptCommandBase(icon: ImageReference) : ActiveStateToolbarCommand(CommandName.SUPERSCRIPT, icon) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.setSuperscript()
    }

}