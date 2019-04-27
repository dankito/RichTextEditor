package net.dankito.richtexteditor.java.fx.command

import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.command.SubscriptCommandBase
import net.dankito.richtexteditor.java.fx.JavaFXIcon
import net.dankito.richtexteditor.java.fx.extensions.addKeyboardShortcut


open class SubscriptCommand(icon: Icon = JavaFXIcon.fromResourceName("ic_format_subscript_black_36dp.png")) : SubscriptCommandBase(icon) {


    init {
        this.addKeyboardShortcut(KeyCodeCombination(KeyCode.T, KeyCombination.SHORTCUT_DOWN)) {
            commandInvoked()
        }
    }

}