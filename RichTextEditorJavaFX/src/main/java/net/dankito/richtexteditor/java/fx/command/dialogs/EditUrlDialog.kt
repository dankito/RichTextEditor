package net.dankito.richtexteditor.java.fx.command.dialogs

import javafx.stage.Window
import net.dankito.richtexteditor.java.fx.localization.Localization


open class EditUrlDialog(localization: Localization, val urlEnteredListener: (url: String, title: String) -> Unit)
    : EnterTwoStringsDialogBase(localization, "dialog.edit.url.url.label", "dialog.edit.url.title.label",
        "dialog.edit.url.dialog.title") {

    companion object {
        fun show(localization: Localization, owner: Window? = null, urlEnteredListener: (url: String, title: String) -> Unit) {
            EditUrlDialog(localization, urlEnteredListener).show(owner)
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