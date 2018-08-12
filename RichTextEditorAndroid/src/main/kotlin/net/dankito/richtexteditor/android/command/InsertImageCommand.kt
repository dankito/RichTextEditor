package net.dankito.richtexteditor.android.command

import android.support.v4.app.FragmentActivity
import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.android.AndroidIcon
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.android.command.dialogs.EditImageDialog
import net.dankito.richtexteditor.command.InsertImageCommandBase
import net.dankito.richtexteditor.model.DownloadImageUiSetting


class InsertImageCommand(icon: Icon = AndroidIcon(R.drawable.ic_insert_photo_white_48dp)) : InsertImageCommandBase(icon), ICommandRequiringEditor {

    override var editor: RichTextEditor? = null

    override fun selectImageToInsert(imageSelected: (imageUrl: String, alternateText: String) -> Unit) {
        (editor?.context as? FragmentActivity)?.let { activity ->
            val dialog = EditImageDialog()

            dialog.show(activity.supportFragmentManager, DownloadImageUiSetting.AllowSelectDownloadFolderInCode) { imageUrl, alternateText ->
                imageSelected(imageUrl, alternateText)
            }
        }
    }

}