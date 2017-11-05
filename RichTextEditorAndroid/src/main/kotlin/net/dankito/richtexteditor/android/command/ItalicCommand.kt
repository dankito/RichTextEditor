package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor


class ItalicCommand : ActiveStateToolbarCommand(Command.ITALIC, R.drawable.ic_format_italic_white_48dp) {

    override fun executeCommand(editor: RichTextEditor) {
        editor.setItalic()
    }

}