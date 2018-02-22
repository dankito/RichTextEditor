package net.dankito.richtexteditor.java.fx.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.command.InsertImageCommandBase
import net.dankito.richtexteditor.java.fx.JavaFXIcon
import net.dankito.richtexteditor.java.fx.command.dialogs.EditImageDialog
import net.dankito.richtexteditor.java.fx.localization.Localization


class InsertImageCommand(private val localization: Localization, icon: Icon = JavaFXIcon.fromResourceName("ic_insert_photo_black_36dp.png"))
    : InsertImageCommandBase(icon) {

    override fun selectImageToInsert(imageSelected: (imageUrl: String, alternateText: String) -> Unit) {
        EditImageDialog.show(localization, imageSelected)
    }

}