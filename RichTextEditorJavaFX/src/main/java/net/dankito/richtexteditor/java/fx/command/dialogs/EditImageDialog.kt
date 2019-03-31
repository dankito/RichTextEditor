package net.dankito.richtexteditor.java.fx.command.dialogs

import javafx.stage.FileChooser
import javafx.stage.Window
import net.dankito.richtexteditor.java.fx.localization.Localization
import net.dankito.utils.io.FileUtils


open class EditImageDialog(private val localization: Localization, private val imageUrlEnteredListener: (imageUrl: String, alternateText: String) -> Unit)
    : EnterTwoStringsDialogBase(localization, "dialog.edit.image.image.url.label", "dialog.edit.image.alternate.text.label",
        "dialog.edit.image.dialog.title") {

    companion object {
        fun show(localization: Localization, owner: Window? = null, imageUrlEnteredListener: (imageUrl: String, alternateText: String) -> Unit) {
            EditImageDialog(localization, imageUrlEnteredListener).show(owner)
        }
    }


    protected val fileUtils = FileUtils()


    init {
        isSelectLocalFileButtonVisible.set(true)
    }


    override fun enteringStringsDone(valueOne: String, valueTwo: String) {
        val imageUrl = valueOne
        var alternateText = valueTwo.trim()

        if(alternateText.isBlank()) {
            alternateText = imageUrl
        }

        imageUrlEnteredListener(imageUrl, alternateText)
    }


    override fun isOkButtonEnabled(stringOne: String, stringTwo: String): Boolean {
        return isValidHttpUrl(stringOne) || fileUtils.isExistingFile(stringOne)
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