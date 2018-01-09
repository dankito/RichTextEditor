package net.dankito.richtexteditor.java.fx.command

import net.dankito.richtexteditor.Color
import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.command.SetTextBackgroundColorCommandBase
import net.dankito.richtexteditor.java.fx.JavaFXIcon


class SetTextBackgroundColorCommand(defaultColor: Color = Color.Transparent, icon: Icon = JavaFXIcon.fromResourceName("ic_format_color_fill_black_36dp.png"), showColorInCommandView: Boolean = true)
    : SetTextBackgroundColorCommandBase(icon, defaultColor, showColorInCommandView) {

    override fun selectColor(currentColor: Color, colorSelected: (Color) -> Unit) {
        // nothing to do here
    }

}