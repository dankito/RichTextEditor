package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor


class UndoCommand(iconResourceId: Int = R.drawable.ic_undo_white_48dp) : ToolbarCommand(Command.UNDO, iconResourceId) {

    override fun executeCommand(editor: RichTextEditor) {
        editor.undo()
    }

}