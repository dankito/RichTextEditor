package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.command.Command


class StrikeThroughCommand(iconResourceId: Int = R.drawable.ic_format_strikethrough_white_48dp) : ActiveStateToolbarCommand(Command.STRIKETHROUGH, iconResourceId) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.setStrikeThrough()
    }

}