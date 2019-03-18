package net.dankito.richtexteditor.command

import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.JavaScriptExecutorBase

abstract class SubscriptCommandBase(icon: ImageReference) : ActiveStateToolbarCommand(CommandName.SUBSCRIPT, icon) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.setSubscript()
    }

}