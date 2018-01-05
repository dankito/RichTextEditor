package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.command.Command


class UnderlineCommand(iconResourceId: Int = R.drawable.ic_format_underlined_white_48dp) : ActiveStateToolbarCommand(Command.UNDERLINE, iconResourceId) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.setUnderline()
    }

}