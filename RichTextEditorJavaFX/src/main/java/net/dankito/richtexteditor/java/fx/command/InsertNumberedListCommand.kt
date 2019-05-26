package net.dankito.richtexteditor.java.fx.command

import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.command.InsertNumberedListCommandBase
import net.dankito.utils.javafx.ui.image.JavaFXImageReference
import net.dankito.richtexteditor.java.fx.extensions.addKeyboardShortcut


open class InsertNumberedListCommand(icon: ImageReference = JavaFXImageReference.fromIconsResourceName("ic_format_list_numbered_black_36dp.png")) : InsertNumberedListCommandBase(icon) {


    init {
        this.addKeyboardShortcut(KeyCodeCombination(KeyCode.G, KeyCombination.SHORTCUT_DOWN)) {
            commandInvoked()
        }
    }

}