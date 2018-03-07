package net.dankito.richtexteditor.command

import net.dankito.richtexteditor.Color
import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase


abstract class SwitchColorOnOffCommand(private val offColor: Color, private var onColor: Color, showColorInCommandView: Boolean = true, private val setOnColorToCurrentColor: Boolean = true,
                                       command: CommandName, icon: Icon, style: ToolbarCommandStyle = ToolbarCommandStyle(), commandExecutedListener: (() -> Unit)? = null)
    : ColorCommand(offColor, showColorInCommandView, command, icon, style, commandExecutedListener) {


    abstract fun applyColor(executor: JavaScriptExecutorBase, color: Color)



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


    override fun setCommandViewBackgroundColor(color: Color) {
        if(setOnColorToCurrentColor == true || color == offColor || color == onColor) { // if setOnColorToCurrentColor is set to false don't set background color to HTML's background color, only to offColor and onColor
            super.setCommandViewBackgroundColor(color)
        }
        else if(setOnColorToCurrentColor == false && color != onColor) { // then set background color to offColor, not to HTML's background color
            super.setCommandViewBackgroundColor(offColor)
        }
    }

}