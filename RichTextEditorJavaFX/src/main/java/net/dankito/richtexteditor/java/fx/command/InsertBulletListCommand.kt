package net.dankito.richtexteditor.java.fx.command

import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.command.InsertBulletListCommandBase
import net.dankito.utils.javafx.ui.image.JavaFXImageReference
import net.dankito.richtexteditor.java.fx.extensions.addKeyboardShortcut


class InsertBulletListCommand(icon: ImageReference = JavaFXImageReference.fromIconsResourceName("ic_format_list_bulleted_black_36dp.png")) : InsertBulletListCommandBase(icon) {


    init {
        this.addKeyboardShortcut(KeyCodeCombination(KeyCode.D, KeyCombination.SHORTCUT_DOWN)) {
            commandInvoked()
        }
    }

}