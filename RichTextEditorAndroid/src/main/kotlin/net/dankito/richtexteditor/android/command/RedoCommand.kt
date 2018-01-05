package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.command.Command


class RedoCommand(iconResourceId: Int = R.drawable.ic_redo_white_48dp) : ToolbarCommand(Command.REDO, iconResourceId) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.redo()
    }

}