package net.dankito.richtexteditor.java.fx.command

import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.command.InsertLinkCommandBase
import net.dankito.richtexteditor.java.fx.JavaFXIcon
import net.dankito.richtexteditor.java.fx.command.dialogs.EditUrlDialog
import net.dankito.richtexteditor.java.fx.extensions.addKeyboardShortcut
import net.dankito.richtexteditor.java.fx.localization.Localization
import net.dankito.richtexteditor.java.fx.toolbar.EditorToolbar


class InsertLinkCommand(private val toolbar: EditorToolbar, private val localization: Localization, icon: Icon = JavaFXIcon.fromResourceName("ic_insert_link_black_36dp.png"))
    : InsertLinkCommandBase(icon) {


    init {
        this.addKeyboardShortcut(KeyCodeCombination(KeyCode.K, KeyCombination.SHORTCUT_DOWN)) {
            commandInvoked()
        }
    }


    override fun selectLinkToInsert(linkSelected: (url: String, title: String) -> Unit) {
        EditUrlDialog.show(localization, toolbar.root.scene?.window, linkSelected)
    }

}