package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.command.Command

class SubscriptCommand(iconResourceId: Int = R.drawable.ic_format_subscript) : ActiveStateToolbarCommand(Command.SUBSCRIPT, iconResourceId) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.setSubscript()
    }

}