package net.dankito.richtexteditor.android.command

import android.support.v4.app.FragmentActivity
import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.android.AndroidIcon
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.android.command.dialogs.EditUrlDialog
import net.dankito.richtexteditor.command.InsertLinkCommandBase


open class InsertLinkCommand(icon: Icon = AndroidIcon(R.drawable.ic_insert_link_white_48dp)) : InsertLinkCommandBase(icon), ICommandRequiringEditor {

    override var editor: RichTextEditor? = null


    override fun selectLinkToInsert(linkSelected: (url: String, title: String) -> Unit) {
        (editor?.context as? FragmentActivity)?.let { activity ->
            val dialog = EditUrlDialog()

            dialog.show(activity.supportFragmentManager) { url, title ->
                linkSelected(url, title)
            }
        }
    }

}