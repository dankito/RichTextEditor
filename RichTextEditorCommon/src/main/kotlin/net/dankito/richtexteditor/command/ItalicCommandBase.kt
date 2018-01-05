package net.dankito.richtexteditor.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase


abstract class ItalicCommandBase(icon: Icon) : ActiveStateToolbarCommand(CommandName.ITALIC, icon) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.setItalic()
    }

}