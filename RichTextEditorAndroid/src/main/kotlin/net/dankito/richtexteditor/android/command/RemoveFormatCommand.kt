package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.android.AndroidIcon
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.command.Command


class RemoveFormatCommand(icon: Icon = AndroidIcon(R.drawable.ic_format_clear_white_48dp)) : ToolbarCommand(Command.REMOVEFORMAT, icon) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.removeFormat()
    }

}