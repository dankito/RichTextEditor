package net.dankito.richtexteditor.android.command

import android.widget.ImageView
import net.dankito.richtexteditor.android.RichTextEditor


abstract class ToolbarCommand(val command: Command,
                              val iconResourceId: Int,
                              val style: ToolbarCommandStyle = ToolbarCommandStyle(),
                              val commandExecutedListener: (() -> Unit)? = null) {


    var editor: RichTextEditor? = null
        set(value) {
            field = value

            value?.addCommandStatesChangedListener { commandStatesUpdated(it) }
        }

    var commandView: ImageView? = null

    var isExecutable: Boolean = true


    fun commandInvoked() {
        editor?.let {
            executeCommand(it)
        }

        commandExecutedListener?.invoke()
    }

    abstract protected fun executeCommand(editor: RichTextEditor)


    private fun commandStatesUpdated(commandStates: Map<Command, CommandState>) {
        commandStates[command]?.let { commandState ->
            commandView?.let { commandView ->
                showCommandExecutableState(commandView, commandState.executable)

                if(commandState.value != "") {
                    commandValueChanged(commandView, commandState.value)
                }
            }
        }
    }

    private fun showCommandExecutableState(commandView: ImageView, executable: Boolean) {
        this.isExecutable = executable
        commandView.isEnabled = executable

        setIconTintColorToExecutableState(commandView, executable)
    }

    protected fun setIconTintColorToExecutableState(commandView: ImageView, isExecutable: Boolean) {
        if(isExecutable) {
            commandView.setColorFilter(style.enabledTintColor)
        }
        else {
            commandView.setColorFilter(style.disabledTintColor)
        }
    }

    protected open fun commandValueChanged(commandView: ImageView, commandValue: Any) {

    }

}