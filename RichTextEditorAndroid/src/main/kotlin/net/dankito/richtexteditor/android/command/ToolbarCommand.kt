package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.android.CommandView
import net.dankito.richtexteditor.command.Command
import net.dankito.richtexteditor.command.CommandState


abstract class ToolbarCommand(val command: Command,
                              val iconResourceId: Int,
                              val style: ToolbarCommandStyle = ToolbarCommandStyle(),
                              val commandExecutedListener: (() -> Unit)? = null) {


    var executor: JavaScriptExecutorBase? = null
        set(value) {
            field = value

            value?.addCommandStatesChangedListener { commandStatesUpdated(it) }
        }

    var commandView: CommandView? = null

    var isExecutable: Boolean = true


    fun commandInvoked() {
        executor?.let {
            executeCommand(it)
        }

        commandExecutedListener?.invoke()
    }

    abstract protected fun executeCommand(executor: JavaScriptExecutorBase)


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

    private fun showCommandExecutableState(commandView: CommandView, executable: Boolean) {
        this.isExecutable = executable
        commandView.setIsEnabled(executable)

        setIconTintColorToExecutableState(commandView, executable)
    }

    protected fun setIconTintColorToExecutableState(commandView: CommandView, isExecutable: Boolean) {
        if(isExecutable) {
            commandView.setTintColor(style.enabledTintColor)
        }
        else {
            commandView.setTintColor(style.disabledTintColor)
        }
    }

    protected open fun commandValueChanged(commandView: CommandView, commandValue: Any) {

    }

}