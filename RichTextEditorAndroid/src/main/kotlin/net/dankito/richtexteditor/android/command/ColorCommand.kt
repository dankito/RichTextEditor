package net.dankito.richtexteditor.android.command

import android.graphics.Color
import android.widget.ImageView
import org.slf4j.LoggerFactory

abstract class ColorCommand(defaultColor: Int, private val showColorInCommandView: Boolean = true, command: Command, iconResourceId: Int, style:
                            ToolbarCommandStyle = ToolbarCommandStyle(), commandExecutedListener: (() -> Unit)? = null)
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
        if(command == Command.FORECOLOR) {
            return Color.BLACK // TODO: is this really true?
        }
        else if(command == Command.BACKCOLOR) {
            return Color.TRANSPARENT
        }

        return null
    }


    protected open fun currentColorChanged(color: Int) {
        this.currentColor = color

        if(showColorInCommandView) {
            setCommandViewBackgroundColor(color)
        }
    }

    private fun setCommandViewBackgroundColor(color: Int) {
        commandView?.let { commandView ->
            commandView.setBackgroundColor(color)

            if(isExecutable && color == Color.WHITE && style.enabledTintColor == Color.WHITE) {
                if(style.isActivatedColor != Color.WHITE) {
                    commandView.setColorFilter(style.isActivatedColor)
                }
                else {
                    commandView.setColorFilter(Color.BLACK) // looks quite ugly to me
                }
            }
            else if(isExecutable && color == Color.BLACK && style.enabledTintColor == Color.BLACK) {
                commandView.setColorFilter(Color.WHITE)
            }
            else {
                setIconTintColorToExecutableState(commandView, isExecutable)
            }
        }
    }

}