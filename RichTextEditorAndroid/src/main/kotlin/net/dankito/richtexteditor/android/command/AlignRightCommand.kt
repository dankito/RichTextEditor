package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor


class AlignRightCommand(iconResourceId: Int = R.drawable.ic_format_align_right_white_48dp) : ActiveStateToolbarCommand(Command.JUSTIFYRIGHT, iconResourceId) {

    override fun executeCommand(editor: RichTextEditor) {
        editor.setJustifyRight()
    }

}