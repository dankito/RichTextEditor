package net.dankito.richtexteditor.command

import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.JavaScriptExecutorBase


abstract class AlignCenterCommandBase(icon: ImageReference) : ActiveStateToolbarCommand(CommandName.JUSTIFYCENTER, icon) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.setJustifyCenter()
    }

}