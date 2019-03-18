package net.dankito.richtexteditor.command

import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.JavaScriptExecutorBase


abstract class BlockQuoteCommandBase(icon: ImageReference) : ActiveStateToolbarCommand(CommandName.BLOCKQUOTE, icon) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.setBlockQuote()
    }

}