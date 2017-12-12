package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor


class InsertBulletListCommand(iconResourceId: Int = R.drawable.ic_format_list_bulleted_white_48dp) : ActiveStateToolbarCommand(Command.INSERTUNORDEREDLIST, iconResourceId) {

    override fun executeCommand(editor: RichTextEditor) {
        editor.insertBulletList()
    }

}