package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.command.Command


class BlockQuoteCommand(iconResourceId: Int = R.drawable.ic_format_quote_white_48dp) : ActiveStateToolbarCommand(Command.BLOCKQUOTE, iconResourceId) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.setBlockQuote()
    }

}