package net.dankito.richtexteditor.command

import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.JavaScriptExecutorBase


abstract class AlignRightCommandBase(icon: ImageReference) : ActiveStateToolbarCommand(CommandName.JUSTIFYRIGHT, icon) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.setJustifyRight()
    }

}