package net.dankito.richtexteditor.android.command

import android.support.v4.app.FragmentActivity
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.android.command.dialogs.EditUrlDialog


class InsertLinkCommand(iconResourceId: Int = R.drawable.ic_insert_link_white_48dp) : ToolbarCommand(Command.INSERTLINK, iconResourceId) {


    override fun executeCommand(editor: RichTextEditor) {
        (editor.context as? FragmentActivity)?.let { activity ->
            val dialog = EditUrlDialog()

            dialog.show(activity.supportFragmentManager) { url, title ->
                editor.insertLink(url, title)
            }
        }
    }

}