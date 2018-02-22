package net.dankito.richtexteditor.java.fx.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.command.InsertLinkCommandBase
import net.dankito.richtexteditor.java.fx.JavaFXIcon
import net.dankito.richtexteditor.java.fx.command.dialogs.EditUrlDialog
import net.dankito.richtexteditor.java.fx.localization.Localization


class InsertLinkCommand(private val localization: Localization, icon: Icon = JavaFXIcon.fromResourceName("ic_insert_link_black_36dp.png"))
    : InsertLinkCommandBase(icon) {

    override fun selectLinkToInsert(linkSelected: (url: String, title: String) -> Unit) {
        EditUrlDialog.show(localization, linkSelected)
    }

}