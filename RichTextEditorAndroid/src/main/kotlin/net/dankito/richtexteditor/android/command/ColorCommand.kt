package net.dankito.richtexteditor.android.command

import android.graphics.Color
import android.widget.ImageView
import org.slf4j.LoggerFactory

abstract class ColorCommand(defaultColor: Int, command: Commands, iconResourceId: Int, style: ToolbarCommandStyle = ToolbarCommandStyle(), commandExecutedListener: (() -> Unit)? = null)
    : ToolbarCommand(command, iconResourceId, style, commandExecutedListener) {

    companion object {
        private val log = LoggerFactory.getLogger(ColorCommand::class.java)
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
        else if(colorString == "inherit") {
            return getColorValueForInherit()
        }

        return null
    }

    protected open fun getColorValueForInherit(): Int? {
        if(command == Commands.FORECOLOR) {
            return Color.BLACK // TODO: is this really true?
        }
        else if(command == Commands.BACKCOLOR) {
            return Color.TRANSPARENT
        }

        return null
    }


    protected open fun currentColorChanged(color: Int) {
        this.currentColor = color
    }

}