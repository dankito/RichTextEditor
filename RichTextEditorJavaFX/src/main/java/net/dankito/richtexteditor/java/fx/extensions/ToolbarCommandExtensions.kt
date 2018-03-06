package net.dankito.richtexteditor.java.fx.extensions

import javafx.application.Platform
import javafx.scene.input.KeyCombination
import net.dankito.richtexteditor.command.ToolbarCommand
import tornadofx.*


fun ToolbarCommand.addKeyboardShortcut(shortcut: KeyCombination, action: (KeyCombination) -> Unit) {
    Platform.runLater { // on init scene is not set yet
        FX.primaryStage.scene?.accelerators?.put(shortcut, Runnable {
            action(shortcut)
        })
    }
}