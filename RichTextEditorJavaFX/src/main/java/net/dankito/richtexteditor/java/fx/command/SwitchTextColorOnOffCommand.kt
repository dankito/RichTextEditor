package net.dankito.richtexteditor.java.fx.command

import net.dankito.utils.Color
import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.command.CommandName
import net.dankito.richtexteditor.command.SwitchColorOnOffCommand
import net.dankito.richtexteditor.java.fx.JavaFXIcon


class SwitchTextColorOnOffCommand(offColor: Color = Color.Black, onColor: Color = Color.Red, icon: Icon = JavaFXIcon.fromResourceName("ic_format_color_text_black_36dp.png"),
                                  showColorInCommandView: Boolean = true, setOnColorToCurrentColor: Boolean = true)
    : SwitchColorOnOffCommand(offColor, onColor, showColorInCommandView, setOnColorToCurrentColor, CommandName.FORECOLOR, icon) {

    override fun applyColor(executor: JavaScriptExecutorBase, color: Color) {
        executor.setTextColor(color)
    }

}