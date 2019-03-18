package net.dankito.richtexteditor.command

import net.dankito.utils.Color
import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.JavaScriptExecutorBase


abstract class SetTextBackgroundColorCommandBase(icon: ImageReference, defaultColor: Color = Color.Transparent, showColorInCommandView: Boolean = true)
    : SetColorCommand(defaultColor, showColorInCommandView, CommandName.BACKCOLOR, icon) {

    override fun applySelectedColor(executor: JavaScriptExecutorBase, color: Color) {
        executor.setTextBackgroundColor(color)
    }

}