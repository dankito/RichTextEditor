package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.command.Command


class BoldCommand(iconResourceId: Int = R.drawable.ic_format_bold_white_48dp) : ActiveStateToolbarCommand(Command.BOLD, iconResourceId) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.setBold()
    }

}