package net.dankito.richtexteditor.java.fx.command.dialogs

import javafx.stage.FileChooser
import net.dankito.richtexteditor.java.fx.localization.Localization
import java.io.File


class EditImageDialog(private val localization: Localization, private val imageUrlEnteredListener: (imageUrl: String, alternateText: String) -> Unit)
    : EnterTwoStringsDialogBase(localization, "dialog.edit.image.image.url.label", "dialog.edit.image.alternate.text.label",
        "dialog.edit.image.dialog.title") {

    companion object {
        fun show(localization: Localization, imageUrlEnteredListener: (imageUrl: String, alternateText: String) -> Unit) {
            EditImageDialog(localization, imageUrlEnteredListener).show()
        }
    }


    init {
        isSelectLocalFileButtonVisible.set(true)
    }


    override fun enteringStringsDone(valueOne: String, valueTwo: String) {
        var imageUrl = valueOne
        if(isExistingFile(imageUrl) && imageUrl.startsWith("file:/") == false) {
            try {
                imageUrl = File(imageUrl).toURI().toString() // add file:/ scheme
            } catch(ignored: Exception) { }
        }

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
        return localization.getLocalizedString("dialog.edit.image.select.image.dialog.title")
    }

    override fun getSelectLocalFileExtensionFilters(): Collection<FileChooser.ExtensionFilter> {
        return listOf(
                FileChooser.ExtensionFilter(localization.getLocalizedString("dialog.edit.image.select.image.dialog.all.images.extension.filter"),
                        "*.jpeg", "*.jpg", "*.JPG", "*.JPEG",
                        "*.png, *.PNG", "*.gif", "*.GIF", "*.bmp", "*.BMP", "*.ico", "*.ICO"),
                FileChooser.ExtensionFilter("JPG (*.jpg)", "*.JPG", "*.jpg", "*.JPEG", "*.jpeg"),
                FileChooser.ExtensionFilter("PNG (*.png)", "*.png", "*.PNG"),
//                FileChooser.ExtensionFilter("TIFF (*.tiff)", "*.TIF", "*.TIFF", "*.tif", "*.tiff"),
                FileChooser.ExtensionFilter("GIF (*.gif)", "*.gif", "*.GIF"),
                FileChooser.ExtensionFilter("BMP (*.bmp)", "*.bmp", "*.BMP"),
                FileChooser.ExtensionFilter("ICO (*.icon)", "*.ico", "*.ICO")
        )
    }

}