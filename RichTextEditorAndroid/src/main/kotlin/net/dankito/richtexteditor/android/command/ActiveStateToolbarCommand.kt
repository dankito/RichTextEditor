package net.dankito.richtexteditor.android.command

import android.widget.ImageView


abstract class ActiveStateToolbarCommand(command: Commands, iconResourceId: Int, style: ToolbarCommandStyle = ToolbarCommandStyle(), commandExecutedListener: (() -> Unit)? = null)
    : Command(command, iconResourceId, style, commandExecutedListener) {


    override fun commandValueChanged(commandView: ImageView, commandValue: Any) {
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