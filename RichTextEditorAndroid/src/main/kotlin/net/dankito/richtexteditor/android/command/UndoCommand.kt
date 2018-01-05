package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.android.AndroidIcon
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.command.CommandName
import net.dankito.richtexteditor.command.ToolbarCommand


class UndoCommand(icon: Icon = AndroidIcon(R.drawable.ic_undo_white_48dp)) : ToolbarCommand(CommandName.UNDO, icon) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.undo()
    }

}