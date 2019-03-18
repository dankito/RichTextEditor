package net.dankito.richtexteditor.java.fx.command

import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.command.SuperscriptCommandBase
import net.dankito.utils.javafx.ui.image.JavaFXImageReference
import net.dankito.richtexteditor.java.fx.extensions.addKeyboardShortcut


class SuperscriptCommand(icon: ImageReference = JavaFXImageReference.fromIconsResourceName("ic_format_superscript_black_36dp.png")) : SuperscriptCommandBase(icon) {


    init {
        this.addKeyboardShortcut(KeyCodeCombination(KeyCode.H, KeyCombination.SHORTCUT_DOWN)) {
            commandInvoked()
        }
    }

}