package net.dankito.richtexteditor.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase


abstract class StrikeThroughCommandBase(icon: Icon) : ActiveStateToolbarCommand(CommandName.STRIKETHROUGH, icon) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.setStrikeThrough()
    }

}