package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.command.Command


class AlignLeftCommand(iconResourceId: Int = R.drawable.ic_format_align_left_white_48dp) : ActiveStateToolbarCommand(Command.JUSTIFYLEFT, iconResourceId) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.setJustifyLeft()
    }

}