package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor


class RedoCommand : ToolbarCommand(Command.REDO, R.drawable.ic_redo_white_48dp) {

    override fun executeCommand(editor: RichTextEditor) {
        editor.redo()
    }

}