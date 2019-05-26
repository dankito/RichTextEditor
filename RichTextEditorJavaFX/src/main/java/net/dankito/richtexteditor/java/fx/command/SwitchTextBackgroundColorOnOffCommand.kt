package net.dankito.richtexteditor.java.fx.command

import net.dankito.utils.Color
import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.command.CommandName
import net.dankito.richtexteditor.command.SwitchColorOnOffCommand
import net.dankito.utils.javafx.ui.image.JavaFXImageReference


open class SwitchTextBackgroundColorOnOffCommand(offColor: Color = Color.Transparent, onColor: Color = Color.Yellow, icon: ImageReference = JavaFXImageReference.fromIconsResourceName("ic_format_color_fill_black_36dp.png"),
                                            showColorInCommandView: Boolean = true, setOnColorToCurrentColor: Boolean = true)
    : SwitchColorOnOffCommand(offColor, onColor, showColorInCommandView, setOnColorToCurrentColor, CommandName.BACKCOLOR, icon) {

    override fun applyColor(executor: JavaScriptExecutorBase, color: Color) {
        executor.setTextBackgroundColor(color)
    }

}