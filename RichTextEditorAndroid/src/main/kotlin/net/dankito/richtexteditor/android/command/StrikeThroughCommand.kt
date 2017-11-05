package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor


class StrikeThroughCommand : ActiveStateToolbarCommand(Command.STRIKETHROUGH, R.drawable.ic_format_strikethrough_white_48dp) {

    override fun executeCommand(editor: RichTextEditor) {
        editor.setStrikeThrough()
    }

}