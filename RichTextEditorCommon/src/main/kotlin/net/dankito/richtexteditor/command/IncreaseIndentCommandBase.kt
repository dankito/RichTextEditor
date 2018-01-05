package net.dankito.richtexteditor.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase


abstract class IncreaseIndentCommandBase(icon: Icon) : ToolbarCommand(CommandName.INDENT, icon) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.setIndent()
    }

}