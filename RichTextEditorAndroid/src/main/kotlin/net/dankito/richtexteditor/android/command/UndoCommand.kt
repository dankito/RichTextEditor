package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor


class UndoCommand : ToolbarCommand(Commands.UNDO, R.drawable.ic_undo_white_48dp) {

    override fun executeCommand(editor: RichTextEditor) {
        editor.undo()
    }

}