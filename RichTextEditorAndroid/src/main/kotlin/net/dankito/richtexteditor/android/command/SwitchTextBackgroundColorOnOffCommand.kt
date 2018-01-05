package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.Color
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.command.Command


class SwitchTextBackgroundColorOnOffCommand(offColor: Color = Color.Transparent, onColor: Color = Color.Yellow, iconResourceId: Int = R.drawable.ic_format_color_fill_white_48dp, showColorInCommandView: Boolean = true)
    : SwitchColorOnOffCommand(offColor, onColor, showColorInCommandView, Command.BACKCOLOR, iconResourceId) {

    override fun applyColor(executor: JavaScriptExecutorBase, color: Color) {
        executor.setTextBackgroundColor(color)
    }

}