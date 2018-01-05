package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.android.AndroidIcon
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.command.ActiveStateToolbarCommand
import net.dankito.richtexteditor.command.CommandName


class InsertBulletListCommand(icon: Icon = AndroidIcon(R.drawable.ic_format_list_bulleted_white_48dp)) : ActiveStateToolbarCommand(CommandName.INSERTUNORDEREDLIST, icon) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.insertBulletList()
    }

}