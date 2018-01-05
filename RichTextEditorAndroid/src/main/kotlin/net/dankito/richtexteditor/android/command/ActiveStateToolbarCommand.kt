package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.android.CommandView
import net.dankito.richtexteditor.command.Command


abstract class ActiveStateToolbarCommand(command: Command, iconResourceId: Int, style: ToolbarCommandStyle = ToolbarCommandStyle(), commandExecutedListener: (() -> Unit)? = null)
    : ToolbarCommand(command, iconResourceId, style, commandExecutedListener) {


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

}