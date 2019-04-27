package net.dankito.richtexteditor.java.fx.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.command.SetTextColorCommandBase
import net.dankito.richtexteditor.java.fx.JavaFXIcon
import net.dankito.utils.Color


open class SetTextColorCommand(defaultColor: Color = Color.Black, icon: Icon = JavaFXIcon.fromResourceName("ic_format_color_text_black_36dp.png"), showColorInCommandView: Boolean = true)
    : SetTextColorCommandBase(icon, defaultColor, showColorInCommandView) {

    override fun selectColor(currentColor: Color, colorSelected: (Color) -> Unit) {
        // nothing to do here
    }

}