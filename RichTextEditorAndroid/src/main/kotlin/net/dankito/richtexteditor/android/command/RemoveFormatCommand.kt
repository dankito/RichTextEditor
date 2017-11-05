package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor


class RemoveFormatCommand : ToolbarCommand(Command.REMOVEFORMAT, R.drawable.ic_format_clear_white_48dp) {

    override fun executeCommand(editor: RichTextEditor) {
        editor.removeFormat()
    }

}