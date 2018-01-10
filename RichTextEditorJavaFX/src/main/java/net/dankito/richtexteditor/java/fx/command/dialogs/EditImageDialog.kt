package net.dankito.richtexteditor.java.fx.command.dialogs

import tornadofx.*
import java.io.File


class EditImageDialog(val imageUrlEnteredListener: (imageUrl: String, alternateText: String) -> Unit)
    : EnterTwoStringsDialogBase(FX.messages["dialog.edit.image.image.url.label"], FX.messages["dialog.edit.image.alternate.text.label"], FX.messages["dialog.edit.image.dialog.title"]) {

    companion object {
        fun show(imageUrlEnteredListener: (imageUrl: String, alternateText: String) -> Unit) {
            EditImageDialog(imageUrlEnteredListener).show()
        }
    }


    override fun enteringStringsDone(valueOne: String, valueTwo: String) {
        imageUrlEnteredListener(valueOne, valueTwo)
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

}