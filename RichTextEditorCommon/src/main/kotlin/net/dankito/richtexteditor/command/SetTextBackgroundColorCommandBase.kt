package net.dankito.richtexteditor.command

import net.dankito.richtexteditor.Color
import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase


abstract class SetTextBackgroundColorCommandBase(icon: Icon, defaultColor: Color = Color.Transparent, showColorInCommandView: Boolean = true)
    : SetColorCommand(defaultColor, showColorInCommandView, CommandName.BACKCOLOR, icon) {

    override fun applySelectedColor(executor: JavaScriptExecutorBase, color: Color) {
        executor.setTextBackgroundColor(color)
    }

}