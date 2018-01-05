package net.dankito.richtexteditor.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase


abstract class InsertBulletListCommandBase(icon: Icon) : ActiveStateToolbarCommand(CommandName.INSERTUNORDEREDLIST, icon) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.insertBulletList()
    }

}