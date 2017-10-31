package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor


class InsertBulletListCommand : Command(Commands.UNORDEREDLIST, R.drawable.ic_format_list_bulleted_white_48dp) {

    override fun executeCommand(editor: RichTextEditor) {
        editor.insertBulletList()
    }

}