package net.dankito.richtexteditor.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase

abstract class SuperscriptCommandBase(icon: Icon) : ActiveStateToolbarCommand(CommandName.SUPERSCRIPT, icon) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.setSuperscript()
    }

}