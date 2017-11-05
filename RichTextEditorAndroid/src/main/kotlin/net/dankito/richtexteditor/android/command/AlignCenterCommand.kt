package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor


class AlignCenterCommand : ActiveStateToolbarCommand(Command.JUSTIFYCENTER, R.drawable.ic_format_align_center_white_48dp) {

    override fun executeCommand(editor: RichTextEditor) {
        editor.setJustifyCenter()
    }

}