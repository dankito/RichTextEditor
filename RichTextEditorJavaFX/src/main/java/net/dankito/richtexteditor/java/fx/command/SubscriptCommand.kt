package net.dankito.richtexteditor.java.fx.command

import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.command.SubscriptCommandBase
import net.dankito.utils.javafx.ui.image.JavaFXImageReference
import net.dankito.richtexteditor.java.fx.extensions.addKeyboardShortcut


class SubscriptCommand(icon: ImageReference = JavaFXImageReference.fromIconsResourceName("ic_format_subscript_black_36dp.png")) : SubscriptCommandBase(icon) {


    init {
        this.addKeyboardShortcut(KeyCodeCombination(KeyCode.T, KeyCombination.SHORTCUT_DOWN)) {
            commandInvoked()
        }
    }

}