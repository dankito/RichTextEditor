package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.android.AndroidIcon
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.command.CommandName
import net.dankito.richtexteditor.command.ToolbarCommand


class RedoCommand(icon: Icon = AndroidIcon(R.drawable.ic_redo_white_48dp)) : ToolbarCommand(CommandName.REDO, icon) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.redo()
    }

}