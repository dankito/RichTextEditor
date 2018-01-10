package net.dankito.richtexteditor.java.fx.command.dialogs

import javafx.stage.FileChooser
import tornadofx.*
import java.io.File


class EditImageDialog(val imageUrlEnteredListener: (imageUrl: String, alternateText: String) -> Unit)
    : EnterTwoStringsDialogBase(FX.messages["dialog.edit.image.image.url.label"], FX.messages["dialog.edit.image.alternate.text.label"], FX.messages["dialog.edit.image.dialog.title"]) {

    companion object {
        fun show(imageUrlEnteredListener: (imageUrl: String, alternateText: String) -> Unit) {
            EditImageDialog(imageUrlEnteredListener).show()
        }
    }


    init {
        isSelectLocalFileButtonVisible.set(true)
    }


    override fun enteringStringsDone(valueOne: String, valueTwo: String) {
        var imageUrl = valueOne

        var alternateText = valueTwo.trim()
        if(alternateText.isBlank()) {
            alternateText = imageUrl
        }

        imageUrlEnteredListener(imageUrl, alternateText)
    }


    override fun isOkButtonEnabled(stringOne: String, stringTwo: String): Boolean {
        return isValidHttpUrl(stringOne) || isExistingFile(stringOne)
    }

    private fun isExistingFile(string: String): Boolean {
        try {
            val file = File(string)
            return file.exists() && file.isFile
        } catch(ignored: Exception) { }

        return false
    }


    override fun getSelectLocalFileTitle(): String {
        return FX.messages["dialog.edit.image.select.image.dialog.title"]
    }

    override fun getSelectLocalFileExtensionFilters(): Collection<FileChooser.ExtensionFilter> {
        return listOf(
                FileChooser.ExtensionFilter(FX.messages["dialog.edit.image.select.image.dialog.all.images.extension.filter"],
                        "*.jpeg", "*.jpg", "*.JPG", "*.JPEG",
                        "*.png, *.PNG", "*.gif", "*.GIF", "*.bmp", "*.BMP", "*.ico", "*.ICO"),
                FileChooser.ExtensionFilter("JPG (*.jpg)", "*.JPG", "*.jpg", "*.JPEG", "*.jpeg"),
                FileChooser.ExtensionFilter("PNG (*.png)", "*.png, *.PNG"),
//                FileChooser.ExtensionFilter("TIFF (*.tiff)", "*.TIF", "*.TIFF", "*.tif", "*.tiff"),
                FileChooser.ExtensionFilter("GIF (*.gif)", "*.gif", "*.GIF"),
                FileChooser.ExtensionFilter("BMP (*.bmp)", "*.bmp", "*.BMP"),
                FileChooser.ExtensionFilter("ICO (*.icon)", "*.ico", "*.ICO")
        )
    }

}