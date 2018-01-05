package net.dankito.richtexteditor.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase


abstract class BlockQuoteCommandBase(icon: Icon) : ActiveStateToolbarCommand(CommandName.BLOCKQUOTE, icon) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.setBlockQuote()
    }

}