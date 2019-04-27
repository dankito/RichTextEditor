package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.android.AndroidIcon
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.command.CommandName
import net.dankito.richtexteditor.command.SwitchColorOnOffCommand
import net.dankito.utils.Color


open class SwitchTextColorOnOffCommand(offColor: Color = Color.Black, onColor: Color = Color.Red, icon: Icon = AndroidIcon(R.drawable.ic_format_color_text_white_48dp),
                                  showColorInCommandView: Boolean = true, setOnColorToCurrentColor: Boolean = true)
    : SwitchColorOnOffCommand(offColor, onColor, showColorInCommandView, setOnColorToCurrentColor, CommandName.FORECOLOR, icon) {

    override fun applyColor(executor: JavaScriptExecutorBase, color: Color) {
        executor.setTextColor(color)
    }

}