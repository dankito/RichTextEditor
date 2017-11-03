package net.dankito.richtexteditor.android.command

import android.graphics.Color
import android.widget.ImageView
import com.azeesoft.lib.colorpicker.ColorPickerDialog
import net.dankito.richtexteditor.android.RichTextEditor
import org.slf4j.LoggerFactory


abstract class SetColorCommand(defaultColor: Int, command: Commands, iconResourceId: Int, style: ToolbarCommandStyle = ToolbarCommandStyle(), commandExecutedListener: (() -> Unit)? = null)
    : Command(command, iconResourceId, style, commandExecutedListener) {

    companion object {
        private val log = LoggerFactory.getLogger(SetColorCommand::class.java)
    }


    protected var currentColor: Int = defaultColor


    override fun commandValueChanged(commandView: ImageView, commandValue: Any) {
        super.commandValueChanged(commandView, commandValue)

        if(commandValue is String) {
            tryToParseColorFromString(commandValue)?.let { color ->
                currentColorChanged(color)
            }
        }
    }

    private fun tryToParseColorFromString(colorString: String): Int? {
        if(colorString.startsWith("rgba(") || colorString.startsWith("rgb(")) {
            try {
                val hexColorString = colorString.replace("rgba(", "").replace("rgb(", "").replace(")", "")
                val hexValues = hexColorString.split(',').map { it.trim() }.filter { it.isNotEmpty() }.map { it.toIntOrNull() }.filterNotNull()

                if (hexValues.size == 3) {
                    return Color.rgb(hexValues[0], hexValues[1], hexValues[2])
                }
                else if (hexValues.size == 4) {
                    return Color.argb(hexValues[3], hexValues[0], hexValues[1], hexValues[2])
                }
            } catch(e: Exception) { log.error("Could not parse color string $colorString", e) }
        }

        return null
    }

    override fun executeCommand(editor: RichTextEditor) {
        val colorPickerDialog = ColorPickerDialog.createColorPickerDialog(editor.context)

        colorPickerDialog.setOnColorPickedListener { color, _ ->
            currentColorChanged(color)

            applySelectedColor(editor, color)
        }

        colorPickerDialog.setInitialColor(currentColor)
        colorPickerDialog.show()
    }


    protected open fun currentColorChanged(color: Int) {
        this.currentColor = color
    }

    protected abstract fun applySelectedColor(editor: RichTextEditor, color: Int)

}