package net.dankito.richtexteditor.android.command

import android.support.v7.app.AppCompatActivity
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.android.command.dialogs.EditUrlDialog

class InsertLinkCommand : Command(Commands.INSERTLINK, R.drawable.ic_insert_link_white_48dp) {


    override fun executeCommand(editor: RichTextEditor) {
        (editor.context as? AppCompatActivity)?.let { activity ->
            val dialog = EditUrlDialog()

            dialog.show(activity.supportFragmentManager) { url, title ->
                editor.insertLink(url, title)
            }
        }
    }

}