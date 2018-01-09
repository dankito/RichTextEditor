package net.dankito.richtexteditor.java.fx.command

import net.dankito.richtexteditor.Color
import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.command.CommandName
import net.dankito.richtexteditor.command.SwitchColorOnOffCommand
import net.dankito.richtexteditor.java.fx.JavaFXIcon


class SwitchTextBackgroundColorOnOffCommand(offColor: Color = Color.Transparent, onColor: Color = Color.Yellow, icon: Icon = JavaFXIcon.fromResourceName("ic_format_color_fill_black_36dp.png"),
                                            showColorInCommandView: Boolean = true) : SwitchColorOnOffCommand(offColor, onColor, showColorInCommandView, CommandName.BACKCOLOR, icon) {

    override fun applyColor(executor: JavaScriptExecutorBase, color: Color) {
        executor.setTextBackgroundColor(color)
    }

}