package net.dankito.richtexteditor.command

import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.JavaScriptExecutorBase


abstract class AlignJustifyCommandBase(icon: ImageReference) : ActiveStateToolbarCommand(CommandName.JUSTIFYFULL, icon) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.setJustifyFull()
    }

}