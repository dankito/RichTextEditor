package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor


class BoldCommand : ActiveStateToolbarCommand(Command.BOLD, R.drawable.ic_format_bold_white_48dp) {

    override fun executeCommand(editor: RichTextEditor) {
        editor.setBold()
    }

}