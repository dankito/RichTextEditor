package net.dankito.richtexteditor.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase


abstract class AlignLeftCommandBase(icon: Icon) : ActiveStateToolbarCommand(CommandName.JUSTIFYLEFT, icon) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.setJustifyLeft()
    }

}