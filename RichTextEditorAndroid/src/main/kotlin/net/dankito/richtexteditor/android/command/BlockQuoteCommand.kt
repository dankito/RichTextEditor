package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor


class BlockQuoteCommand(iconResourceId: Int = R.drawable.ic_format_quote_white_48dp) : ActiveStateToolbarCommand(Command.BLOCKQUOTE, iconResourceId) {

    override fun executeCommand(editor: RichTextEditor) {
        editor.setBlockQuote()
    }

}