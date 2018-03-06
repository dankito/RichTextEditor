package net.dankito.richtexteditor.command

import net.dankito.richtexteditor.Color
import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase


abstract class SwitchColorOnOffCommand(private val offColor: Color, private var onColor: Color, showColorInCommandView: Boolean = true, private val setOnColorToCurrentColor: Boolean = true,
                                       command: CommandName, icon: Icon, style: ToolbarCommandStyle = ToolbarCommandStyle(), commandExecutedListener: (() -> Unit)? = null)
    : ColorCommand(offColor, showColorInCommandView, command, icon, style, commandExecutedListener) {

    override fun currentColorChanged(color: Color) {
        super.currentColorChanged(color)

        if(setOnColorToCurrentColor && color != offColor) {
            this.onColor = color
        }
    }

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        if(currentColor != onColor) {
            applyColor(executor, onColor)
        }
        else {
            applyColor(executor, offColor)
        }
    }


    abstract fun applyColor(executor: JavaScriptExecutorBase, color: Color)

}