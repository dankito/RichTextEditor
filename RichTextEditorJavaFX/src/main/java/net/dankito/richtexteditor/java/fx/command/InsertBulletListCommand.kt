package net.dankito.richtexteditor.java.fx.command

import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.command.InsertBulletListCommandBase
import net.dankito.richtexteditor.java.fx.JavaFXIcon
import net.dankito.richtexteditor.java.fx.extensions.addKeyboardShortcut


class InsertBulletListCommand(icon: Icon = JavaFXIcon.fromResourceName("ic_format_list_bulleted_black_36dp.png")) : InsertBulletListCommandBase(icon) {


    init {
        this.addKeyboardShortcut(KeyCodeCombination(KeyCode.D, KeyCombination.SHORTCUT_DOWN)) {
            commandInvoked()
        }
    }

}