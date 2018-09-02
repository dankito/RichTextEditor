package net.dankito.richtexteditor.android.command

import net.dankito.utils.Color
import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.android.AndroidIcon
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.command.CommandName
import net.dankito.richtexteditor.command.SwitchColorOnOffCommand


open class SwitchTextBackgroundColorOnOffCommand(offColor: Color = Color.Transparent, onColor: Color = Color.Yellow,
                                                 icon: Icon = AndroidIcon(R.drawable.ic_format_color_fill_white_48dp),
                                                 showColorInCommandView: Boolean = true, setOnColorToCurrentColor: Boolean = true)
    : SwitchColorOnOffCommand(offColor, onColor, showColorInCommandView, setOnColorToCurrentColor, CommandName.BACKCOLOR, icon) {

    override fun applyColor(executor: JavaScriptExecutorBase, color: Color) {
        executor.setTextBackgroundColor(color)
    }

}