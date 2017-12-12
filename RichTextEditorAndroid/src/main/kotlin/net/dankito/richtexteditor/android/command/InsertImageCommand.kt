package net.dankito.richtexteditor.android.command

import android.support.v4.app.FragmentActivity
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.android.command.dialogs.EditImageDialog


class InsertImageCommand(iconResourceId: Int = R.drawable.ic_insert_photo_white_48dp) : ToolbarCommand(Command.INSERTIMAGE, iconResourceId) {


    override fun executeCommand(editor: RichTextEditor) {
        (editor.context as? FragmentActivity)?.let { activity ->
            val dialog = EditImageDialog()

            dialog.show(activity.supportFragmentManager) { imageUrl, alternateText ->
                editor.insertImage(imageUrl, alternateText)
            }
        }
    }

}