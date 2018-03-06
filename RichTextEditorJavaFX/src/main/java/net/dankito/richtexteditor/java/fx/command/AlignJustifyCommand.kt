package net.dankito.richtexteditor.java.fx.command

import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.command.AlignJustifyCommandBase
import net.dankito.richtexteditor.java.fx.JavaFXIcon
import net.dankito.richtexteditor.java.fx.extensions.addKeyboardShortcut


class AlignJustifyCommand(icon: Icon = JavaFXIcon.fromResourceName("ic_format_align_justify_black_36dp.png")) : AlignJustifyCommandBase(icon) {


    init {
        this.addKeyboardShortcut(KeyCodeCombination(KeyCode.J, KeyCombination.SHORTCUT_DOWN)) {
            commandInvoked()
        }
    }

}