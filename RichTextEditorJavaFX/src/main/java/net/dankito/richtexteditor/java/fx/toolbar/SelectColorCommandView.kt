package net.dankito.richtexteditor.java.fx.toolbar

import javafx.scene.control.ColorPicker
import javafx.scene.paint.Color
import net.dankito.richtexteditor.command.SetColorCommand
import net.dankito.richtexteditor.command.ToolbarCommand
import net.dankito.richtexteditor.java.fx.extensions.toJavaFXColor
import net.dankito.richtexteditor.java.fx.util.ColorConverter


class SelectColorCommandView(private val command: SetColorCommand, private val commandInvoked: (ToolbarCommand) -> Unit)
    : ColorPicker(command.currentColor.toJavaFXColor()) {

    private val colorConverter = ColorConverter()


    init {
        setOnAction { colorSelected(value) }
    }

    private fun colorSelected(javaFXColor: Color?) {
        javaFXColor?.let { selectedColor ->
            command.executor?.let { executor ->
                command.applySelectedColor(executor, colorConverter.convertJavaFXColor(selectedColor))
            }

            commandInvoked(command)
        }
    }

}