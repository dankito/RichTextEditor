package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor


class AlignRightCommand : Command(Commands.JUSTIFYRIGHT, R.drawable.ic_format_align_right_white_48dp) {

    override fun executeCommand(editor: RichTextEditor) {
        editor.setJustifyRight()
    }

}