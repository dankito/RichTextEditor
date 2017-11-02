package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor


class InsertNumberedListCommand : ActiveStateToolbarCommand(Commands.INSERTORDEREDLIST, R.drawable.ic_format_list_numbered_white_48dp) {

    override fun executeCommand(editor: RichTextEditor) {
        editor.insertNumberedList()
    }

}