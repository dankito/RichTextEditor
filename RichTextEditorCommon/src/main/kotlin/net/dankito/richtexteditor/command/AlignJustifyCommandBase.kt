package net.dankito.richtexteditor.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase


abstract class AlignJustifyCommandBase(icon: Icon) : ActiveStateToolbarCommand(CommandName.JUSTIFYFULL, icon) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.setJustifyFull()
    }

}