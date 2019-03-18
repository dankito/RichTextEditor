package net.dankito.richtexteditor.command

import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.JavaScriptExecutorBase


abstract class UnderlineCommandBase(icon: ImageReference) : ActiveStateToolbarCommand(CommandName.UNDERLINE, icon) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.setUnderline()
    }

}