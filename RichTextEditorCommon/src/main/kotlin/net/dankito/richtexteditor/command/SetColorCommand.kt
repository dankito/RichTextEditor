package net.dankito.richtexteditor.command

import net.dankito.utils.Color
import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase


abstract class SetColorCommand(defaultColor: Color, showColorInCommandView: Boolean = true, command: CommandName, icon: Icon, style: ToolbarCommandStyle = ToolbarCommandStyle(), commandExecutedListener: (() -> Unit)? = null)
    : ColorCommand(defaultColor, showColorInCommandView, command, icon, style, commandExecutedListener) {

    // only needed for Android
    abstract protected fun selectColor(currentColor: Color, colorSelected: (Color) -> Unit)


    override fun executeCommand(executor: JavaScriptExecutorBase) {
        selectColor(currentColor) { selectedColor ->
            currentColorChanged(selectedColor)

            applySelectedColor(executor, selectedColor)
        }
    }


    abstract fun applySelectedColor(executor: JavaScriptExecutorBase, color: Color) // JavaFX directly calls this method

}