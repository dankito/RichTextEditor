package net.dankito.richtexteditor.command

import net.dankito.utils.Color
import net.dankito.richtexteditor.CommandView
import net.dankito.utils.image.ImageReference
import org.slf4j.LoggerFactory

abstract class ColorCommand(defaultColor: Color, private val showColorInCommandView: Boolean = true, command: CommandName, icon: ImageReference, style:
ToolbarCommandStyle = ToolbarCommandStyle(), commandExecutedListener: (() -> Unit)? = null)
    : ToolbarCommand(command, icon, style, commandExecutedListener) {

    companion object {
        private val log = LoggerFactory.getLogger(ColorCommand::class.java)
    }


    var currentColor: Color = defaultColor
        private set


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

    protected open fun setCommandViewBackgroundColor(color: Color) {
        commandView?.let { commandView ->
            commandView.setBackgroundColor(color)

            val visibleBackgroundColor = if(color != Color.Transparent) color else commandView.getParentBackgroundColor()

            if(isExecutable) {
                if(doBackgroundAndTintColorEqual(Color.White, visibleBackgroundColor, commandView)) {
                    commandView.setTintColor(Color.LightGray) // looks quite ugly to me
                }
                else if(doBackgroundAndTintColorEqual(Color.Black, visibleBackgroundColor, commandView)) {
                    commandView.setTintColor(Color.White)
                }
                else {
                    commandView.setTintColor(style.enabledTintColor)
                }
            }
            else {
                setIconTintColorToExecutableState(commandView, isExecutable)
            }
        }
    }

    private fun doBackgroundAndTintColorEqual(colorToTest: Color, visibleBackgroundColor: Color?, commandView: CommandView): Boolean {
        return visibleBackgroundColor == colorToTest && (style.enabledTintColor == colorToTest || commandView.appliedTintColor == colorToTest)
    }

}