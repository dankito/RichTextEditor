package net.dankito.richtexteditor.java.fx.toolbar

import javafx.scene.control.Button
import javafx.scene.image.ImageView
import net.dankito.richtexteditor.command.ToolbarCommand


class ActiveStateCommandView(private val command: ToolbarCommand, private val commandInvoked: (ToolbarCommand) -> Unit) : Button("", ImageView()) {

    init {
        setOnAction { commandClicked() }
    }


    private fun commandClicked() {
        command.commandInvoked()

        commandInvoked(command)
    }

}