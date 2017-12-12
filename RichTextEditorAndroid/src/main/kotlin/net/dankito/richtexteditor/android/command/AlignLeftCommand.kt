package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor


class AlignLeftCommand(iconResourceId: Int = R.drawable.ic_format_align_left_white_48dp) : ActiveStateToolbarCommand(Command.JUSTIFYLEFT, iconResourceId) {

    override fun executeCommand(editor: RichTextEditor) {
        editor.setJustifyLeft()
    }

}