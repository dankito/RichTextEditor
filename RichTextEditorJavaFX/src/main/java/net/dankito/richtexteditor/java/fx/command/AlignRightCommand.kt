package net.dankito.richtexteditor.java.fx.command

import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.command.AlignRightCommandBase
import net.dankito.utils.javafx.ui.image.JavaFXImageReference
import net.dankito.richtexteditor.java.fx.extensions.addKeyboardShortcut


class AlignRightCommand(icon: ImageReference = JavaFXImageReference.fromIconsResourceName("ic_format_align_right_black_36dp.png")) : AlignRightCommandBase(icon) {


    init {
        this.addKeyboardShortcut(KeyCodeCombination(KeyCode.R, KeyCombination.SHORTCUT_DOWN)) {
            commandInvoked()
        }
    }

}