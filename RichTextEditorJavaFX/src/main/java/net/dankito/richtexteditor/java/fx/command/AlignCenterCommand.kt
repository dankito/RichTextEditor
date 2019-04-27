package net.dankito.richtexteditor.java.fx.command

import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.command.AlignCenterCommandBase
import net.dankito.richtexteditor.java.fx.JavaFXIcon
import net.dankito.richtexteditor.java.fx.extensions.addKeyboardShortcut


open class AlignCenterCommand(icon: Icon = JavaFXIcon.fromResourceName("ic_format_align_center_black_36dp.png")) : AlignCenterCommandBase(icon) {


    init {
        this.addKeyboardShortcut(KeyCodeCombination(KeyCode.E, KeyCombination.SHORTCUT_DOWN)) {
            commandInvoked()
        }
    }


}