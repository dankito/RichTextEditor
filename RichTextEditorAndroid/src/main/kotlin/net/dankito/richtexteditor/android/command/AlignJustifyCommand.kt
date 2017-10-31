package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor


class AlignJustifyCommand : Command(Commands.JUSTIFYFULL, R.drawable.ic_format_align_justify_white_48dp) {

    override fun executeCommand(editor: RichTextEditor) {
        editor.setJustifyFull()
    }

}