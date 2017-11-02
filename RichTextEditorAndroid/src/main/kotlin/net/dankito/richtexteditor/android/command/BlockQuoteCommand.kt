package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor


class BlockQuoteCommand : ActiveStateToolbarCommand(Commands.BLOCKQUOTE, R.drawable.ic_format_quote_white_48dp) {

    override fun executeCommand(editor: RichTextEditor) {
        editor.setBlockQuote()
    }

}