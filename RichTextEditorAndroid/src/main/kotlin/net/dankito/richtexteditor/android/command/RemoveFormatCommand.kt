package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.command.Command


class RemoveFormatCommand(iconResourceId: Int = R.drawable.ic_format_clear_white_48dp) : ToolbarCommand(Command.REMOVEFORMAT, iconResourceId) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.removeFormat()
    }

}