package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.Color
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.command.Command

class SwitchTextColorOnOffCommand(offColor: Color = Color.Black, onColor: Color = Color.Red, iconResourceId: Int = R.drawable.ic_format_color_text_white_48dp,
                                  showColorInCommandView: Boolean = true) : SwitchColorOnOffCommand(offColor, onColor, showColorInCommandView, Command.FORECOLOR, iconResourceId) {

    override fun applyColor(executor: JavaScriptExecutorBase, color: Color) {
        executor.setTextColor(color)
    }

}