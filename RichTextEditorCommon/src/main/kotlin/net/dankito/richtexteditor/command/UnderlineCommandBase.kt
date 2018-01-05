package net.dankito.richtexteditor.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase


abstract class UnderlineCommandBase(icon: Icon) : ActiveStateToolbarCommand(CommandName.UNDERLINE, icon) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.setUnderline()
    }

}