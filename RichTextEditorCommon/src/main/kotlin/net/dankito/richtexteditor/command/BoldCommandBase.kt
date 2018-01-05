package net.dankito.richtexteditor.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase


abstract class BoldCommandBase(icon: Icon) : ActiveStateToolbarCommand(CommandName.BOLD, icon) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.setBold()
    }

}