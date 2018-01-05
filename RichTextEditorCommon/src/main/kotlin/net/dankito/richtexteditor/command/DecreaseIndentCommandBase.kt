package net.dankito.richtexteditor.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase


abstract class DecreaseIndentCommandBase(icon: Icon) : ToolbarCommand(CommandName.OUTDENT, icon) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.setOutdent()
    }

}