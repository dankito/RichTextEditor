package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor


class UnderlineCommand : Command(Commands.UNDERLINE, R.drawable.ic_format_underlined_white_48dp) {

    override fun executeCommand(editor: RichTextEditor) {
        editor.setUnderline()
    }

}