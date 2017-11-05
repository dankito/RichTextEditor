package net.dankito.richtexteditor.android.command

import com.azeesoft.lib.colorpicker.ColorPickerDialog
import net.dankito.richtexteditor.android.RichTextEditor


abstract class SetColorCommand(defaultColor: Int, command: Command, iconResourceId: Int, style: ToolbarCommandStyle = ToolbarCommandStyle(), commandExecutedListener: (() -> Unit)? = null)
    : ColorCommand(defaultColor, command, iconResourceId, style, commandExecutedListener) {


    override fun executeCommand(editor: RichTextEditor) {
        val colorPickerDialog = ColorPickerDialog.createColorPickerDialog(editor.context)

        colorPickerDialog.setOnColorPickedListener { color, _ ->
            currentColorChanged(color)

            applySelectedColor(editor, color)
        }

        colorPickerDialog.setInitialColor(currentColor)
        colorPickerDialog.show()
    }


    protected abstract fun applySelectedColor(editor: RichTextEditor, color: Int)

}