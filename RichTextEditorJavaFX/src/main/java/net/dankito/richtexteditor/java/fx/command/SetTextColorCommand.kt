package net.dankito.richtexteditor.java.fx.command

import net.dankito.utils.Color
import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.command.SetTextColorCommandBase
import net.dankito.utils.javafx.ui.image.JavaFXImageReference


class SetTextColorCommand(defaultColor: Color = Color.Black, icon: ImageReference = JavaFXImageReference.fromIconsResourceName("ic_format_color_text_black_36dp.png"), showColorInCommandView: Boolean = true)
    : SetTextColorCommandBase(icon, defaultColor, showColorInCommandView) {

    override fun selectColor(currentColor: Color, colorSelected: (Color) -> Unit) {
        // nothing to do here
    }

}