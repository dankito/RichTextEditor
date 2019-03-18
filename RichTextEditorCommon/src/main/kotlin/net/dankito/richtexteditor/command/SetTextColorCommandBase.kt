package net.dankito.richtexteditor.command

import net.dankito.utils.Color
import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.JavaScriptExecutorBase


abstract class SetTextColorCommandBase(icon: ImageReference, defaultColor: Color = Color.Black, showColorInCommandView: Boolean = true)
    : SetColorCommand(defaultColor, showColorInCommandView, CommandName.FORECOLOR, icon) {

    override fun applySelectedColor(executor: JavaScriptExecutorBase, color: Color) {
        executor.setTextColor(color)
    }

}