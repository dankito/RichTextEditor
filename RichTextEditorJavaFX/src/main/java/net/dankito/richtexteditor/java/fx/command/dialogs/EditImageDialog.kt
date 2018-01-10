package net.dankito.richtexteditor.java.fx.command.dialogs

import tornadofx.*


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

}