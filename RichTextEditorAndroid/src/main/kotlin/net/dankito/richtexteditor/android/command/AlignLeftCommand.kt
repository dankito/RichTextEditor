package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor


class AlignLeftCommand : Command(Commands.JUSTIFYLEFT, R.drawable.ic_format_align_left_white_48dp) {

    override fun executeCommand(editor: RichTextEditor) {
        editor.setJustifyLeft()
    }

}