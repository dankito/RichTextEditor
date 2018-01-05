package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.Color
import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.command.ColorCommand
import net.dankito.richtexteditor.command.CommandName
import net.dankito.richtexteditor.command.ToolbarCommandStyle


abstract class SetColorCommand(defaultColor: Color, showColorInCommandView: Boolean = true, command: CommandName, icon: Icon, style: ToolbarCommandStyle = ToolbarCommandStyle(), commandExecutedListener: (() -> Unit)? = null)
    : ColorCommand(defaultColor, showColorInCommandView, command, icon, style, commandExecutedListener) {

    abstract protected fun selectColor(currentColor: Color, colorSelected: (Color) -> Unit)


    override fun executeCommand(executor: JavaScriptExecutorBase) {
        selectColor(currentColor) { selectedColor ->
            currentColorChanged(selectedColor)

            applySelectedColor(executor, selectedColor)
        }
    }


    protected abstract fun applySelectedColor(executor: JavaScriptExecutorBase, color: Color)

}