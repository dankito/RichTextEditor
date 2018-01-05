package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.Color
import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.android.AndroidIcon
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.command.Command


class SetTextColorCommand(defaultColor: Color = Color.Black, icon: Icon = AndroidIcon(R.drawable.ic_format_color_text_white_48dp), showColorInCommandView: Boolean = true)
    : SetColorCommand(defaultColor, showColorInCommandView, Command.FORECOLOR, icon) {

    override fun applySelectedColor(executor: JavaScriptExecutorBase, color: Color) {
        executor.setTextColor(color)
    }

}