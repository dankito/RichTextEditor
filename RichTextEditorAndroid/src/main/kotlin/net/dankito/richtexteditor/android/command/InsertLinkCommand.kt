package net.dankito.richtexteditor.android.command

import android.support.v4.app.FragmentActivity
import net.dankito.utils.image.ImageReference
import net.dankito.utils.android.image.AndroidImageReference
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.android.command.dialogs.EditUrlDialog
import net.dankito.richtexteditor.command.InsertLinkCommandBase
import net.dankito.utils.android.extensions.asActivity


class InsertLinkCommand(icon: ImageReference = AndroidImageReference(R.drawable.ic_insert_link_white_48dp)) : InsertLinkCommandBase(icon), ICommandRequiringEditor {

    override var editor: RichTextEditor? = null


    override fun selectLinkToInsert(linkSelected: (url: String, title: String) -> Unit) {
        (editor?.context?.asActivity() as? FragmentActivity)?.let { activity ->
            val dialog = EditUrlDialog()

            dialog.show(activity.supportFragmentManager) { url, title ->
                linkSelected(url, title)
            }
        }
    }

}