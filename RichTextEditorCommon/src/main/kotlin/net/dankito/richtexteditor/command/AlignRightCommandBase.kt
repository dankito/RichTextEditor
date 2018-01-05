package net.dankito.richtexteditor.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase


abstract class AlignRightCommandBase(icon: Icon) : ActiveStateToolbarCommand(CommandName.JUSTIFYRIGHT, icon) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.setJustifyRight()
    }

}