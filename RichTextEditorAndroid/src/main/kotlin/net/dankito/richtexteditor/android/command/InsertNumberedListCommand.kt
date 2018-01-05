package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.android.AndroidIcon
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.command.CommandName


class InsertNumberedListCommand(icon: Icon = AndroidIcon(R.drawable.ic_format_list_numbered_white_48dp)) : ActiveStateToolbarCommand(CommandName.INSERTORDEREDLIST, icon) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.insertNumberedList()
    }

}