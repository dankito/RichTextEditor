package net.dankito.richtexteditor.java.fx.command.dialogs

import tornadofx.*


class EditUrlDialog(val urlEnteredListener: (url: String, title: String) -> Unit)
    : EnterTwoStringsDialogBase(FX.messages["dialog.edit.url.url.label"], FX.messages["dialog.edit.url.title.label"], FX.messages["dialog.edit.url.dialog.title"]) {

    companion object {
        fun show(urlEnteredListener: (url: String, title: String) -> Unit) {
            EditUrlDialog(urlEnteredListener).show()
        }
    }


    override fun enteringStringsDone(valueOne: String, valueTwo: String) {
        val url = valueOne

        var title = valueTwo
        if(title.isEmpty()) {
            title = url
        }

        urlEnteredListener.invoke(url, title)
    }


    override fun isOkButtonEnabled(stringOne: String, stringTwo: String): Boolean {
        return isValidHttpUrl(stringOne)
    }

}