package net.dankito.richtexteditor.java.fx.command

import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.command.InsertNumberedListCommandBase
import net.dankito.richtexteditor.java.fx.JavaFXIcon
import net.dankito.richtexteditor.java.fx.extensions.addKeyboardShortcut


class InsertNumberedListCommand(icon: Icon = JavaFXIcon.fromResourceName("ic_format_list_numbered_black_36dp.png")) : InsertNumberedListCommandBase(icon) {


    init {
        this.addKeyboardShortcut(KeyCodeCombination(KeyCode.G, KeyCombination.SHORTCUT_DOWN)) {
            commandInvoked()
        }
    }

}