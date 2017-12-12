package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor


class InsertNumberedListCommand(iconResourceId: Int = R.drawable.ic_format_list_numbered_white_48dp) : ActiveStateToolbarCommand(Command.INSERTORDEREDLIST, iconResourceId) {

    override fun executeCommand(editor: RichTextEditor) {
        editor.insertNumberedList()
    }

}