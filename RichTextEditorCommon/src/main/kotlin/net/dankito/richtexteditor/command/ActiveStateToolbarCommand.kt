package net.dankito.richtexteditor.command

import net.dankito.richtexteditor.CommandView
import net.dankito.richtexteditor.Icon


abstract class ActiveStateToolbarCommand(command: CommandName, icon: Icon, style: ToolbarCommandStyle = ToolbarCommandStyle(), commandExecutedListener: (() -> Unit)? = null)
    : ToolbarCommand(command, icon, style, commandExecutedListener) {


    override fun commandValueChanged(commandView: CommandView, commandValue: Any) {
        super.commandValueChanged(commandView, commandValue)

        val isCommandActivated = commandValue == "true"
        if(isCommandActivated) {
            commandView.setBackgroundColor(style.isActivatedColor)
        }
        else {
            commandView.setBackgroundColor(style.backgroundColor)
        }
    }

    override fun setIconTintColorToExecutableState(commandView: CommandView, isExecutable: Boolean) {
        if(isExecutable) {
            commandView.setTintColor(style.enabledTintColor)
        }
        else {
            commandView.setTintColor(style.disabledTintColor)
        }
    }

}