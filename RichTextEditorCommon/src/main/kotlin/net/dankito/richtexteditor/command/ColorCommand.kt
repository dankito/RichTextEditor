package net.dankito.richtexteditor.command

import net.dankito.richtexteditor.Color
import net.dankito.richtexteditor.CommandView
import net.dankito.richtexteditor.Icon
import org.slf4j.LoggerFactory

abstract class ColorCommand(defaultColor: Color, private val showColorInCommandView: Boolean = true, command: CommandName, icon: Icon, style:
ToolbarCommandStyle = ToolbarCommandStyle(), commandExecutedListener: (() -> Unit)? = null)
    : ToolbarCommand(command, icon, style, commandExecutedListener) {

    companion object {
        private val log = LoggerFactory.getLogger(ColorCommand::class.java)
    }


    protected var currentColor: Color = defaultColor


    override fun commandValueChanged(commandView: CommandView, commandValue: Any) {
        super.commandValueChanged(commandView, commandValue)

        if(commandValue is String) {
            tryToParseColorFromString(commandValue)?.let { color ->
                currentColorChanged(color)
            }
        }
    }

    private fun tryToParseColorFromString(colorString: String): Color? {
        if(colorString.startsWith("rgba(") || colorString.startsWith("rgb(")) {
            try {
                val hexColorString = colorString.replace("rgba(", "").replace("rgb(", "").replace(")", "")
                val hexValues = hexColorString.split(',').map { it.trim() }.filter { it.isNotEmpty() }.map { it.toIntOrNull() }.filterNotNull()

                if (hexValues.size == 3) {
                    return Color(hexValues[0], hexValues[1], hexValues[2])
                }
                else if (hexValues.size == 4) {
                    return Color(hexValues[0], hexValues[1], hexValues[2], hexValues[3])
                }
            } catch(e: Exception) { log.error("Could not parse color string $colorString", e) }
        }
        else if(colorString == "inherit") {
            return getColorValueForInherit()
        }

        return null
    }

    protected open fun getColorValueForInherit(): Color? {
        if(command == CommandName.FORECOLOR) {
            return Color.Black // TODO: is this really true?
        }
        else if(command == CommandName.BACKCOLOR) {
            return Color.Transparent
        }

        return null
    }


    protected open fun currentColorChanged(color: Color) {
        this.currentColor = color

        if(showColorInCommandView) {
            setCommandViewBackgroundColor(color)
        }
    }

    private fun setCommandViewBackgroundColor(color: Color) {
        commandView?.let { commandView ->
            commandView.setBackgroundColor(color)

            if(isExecutable && color == Color.White && style.enabledTintColor == Color.White) {
                if(style.isActivatedColor != Color.White) {
                    commandView.setTintColor(style.isActivatedColor)
                }
                else {
                    commandView.setTintColor(Color.LightGray) // looks quite ugly to me
                }
            }
            else if(isExecutable && ((color == Color.Black && style.enabledTintColor == Color.Black) ||
                    (color != Color.White && color != Color.Transparent))) { // for JavaFX to set icon tint to white as soon as background color is set
                log.info("color = $color")
                commandView.setTintColor(Color.White)
            }
            else {
                setIconTintColorToExecutableState(commandView, isExecutable)
            }
        }
    }

}