package net.dankito.richtexteditor.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase

abstract class SubscriptCommandBase(icon: Icon) : ActiveStateToolbarCommand(CommandName.SUBSCRIPT, icon) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.setSubscript()
    }

}