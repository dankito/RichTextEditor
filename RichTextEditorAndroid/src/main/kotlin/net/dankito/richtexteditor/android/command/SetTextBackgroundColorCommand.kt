package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.Color
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.command.Command


class SetTextBackgroundColorCommand(defaultColor: Color = Color.Transparent, iconResourceId: Int = R.drawable.ic_format_color_fill_white_48dp, showColorInCommandView: Boolean = true)
    : SetColorCommand(defaultColor, showColorInCommandView, Command.BACKCOLOR, iconResourceId) {

    override fun applySelectedColor(executor: JavaScriptExecutorBase, color: Color) {
        executor.setTextBackgroundColor(color)
    }

}