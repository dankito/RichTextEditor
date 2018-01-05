package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.command.Command


class IncreaseIndentCommand(iconResourceId: Int = R.drawable.ic_format_indent_increase_white_48dp) : ToolbarCommand(Command.INDENT, iconResourceId) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.setIndent()
    }

}