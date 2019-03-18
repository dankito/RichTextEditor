package net.dankito.richtexteditor.command

import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.JavaScriptExecutorBase


abstract class AlignLeftCommandBase(icon: ImageReference) : ActiveStateToolbarCommand(CommandName.JUSTIFYLEFT, icon) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.setJustifyLeft()
    }

}