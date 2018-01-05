package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.Color
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.command.Command


abstract class SwitchColorOnOffCommand(private val offColor: Color, private var onColor: Color, showColorInCommandView: Boolean = true, command: Command, iconResourceId: Int, style: ToolbarCommandStyle = ToolbarCommandStyle(),
                                       commandExecutedListener: (() -> Unit)? = null) : ColorCommand(offColor, showColorInCommandView, command, iconResourceId, style, commandExecutedListener) {

    override fun currentColorChanged(color: Color) {
        super.currentColorChanged(color)

        if(color != offColor) {
            this.onColor = color
        }
    }

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        if(currentColor == offColor) {
            applyColor(executor, onColor)
        }
        else {
            applyColor(executor, offColor)
        }
    }


    abstract fun applyColor(executor: JavaScriptExecutorBase, color: Color)

}