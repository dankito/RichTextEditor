package net.dankito.richtexteditor.java.fx.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.command.InsertImageCommandBase
import net.dankito.richtexteditor.java.fx.JavaFXIcon


class InsertImageCommand(icon: Icon = JavaFXIcon.fromResourceName("ic_insert_photo_black_36dp.png")) : InsertImageCommandBase(icon) {

//    override var editor: RichTextEditor? = null

    override fun selectImageToInsert(imageSelected: (imageUrl: String, alternateText: String) -> Unit) {
//        (editor?.context as? FragmentActivity)?.let { activity ->
//            val dialog = EditImageDialog()
//
//            dialog.show(activity.supportFragmentManager) { imageUrl, alternateText ->
//                imageSelected(imageUrl, alternateText)
//            }
//        }
    }

}