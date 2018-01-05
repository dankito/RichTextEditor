package net.dankito.richtexteditor.android.command

import android.support.v4.app.FragmentActivity
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.android.command.dialogs.EditImageDialog
import net.dankito.richtexteditor.command.Command


class InsertImageCommand(iconResourceId: Int = R.drawable.ic_insert_photo_white_48dp) : ToolbarCommand(Command.INSERTIMAGE, iconResourceId), ICommandRequiringEditor {

    override var editor: RichTextEditor? = null


    override fun executeCommand(executor: JavaScriptExecutorBase) {
        (editor?.context as? FragmentActivity)?.let { activity ->
            val dialog = EditImageDialog()

            dialog.show(activity.supportFragmentManager) { imageUrl, alternateText ->
                executor.insertImage(imageUrl, alternateText)
            }
        }
    }

}