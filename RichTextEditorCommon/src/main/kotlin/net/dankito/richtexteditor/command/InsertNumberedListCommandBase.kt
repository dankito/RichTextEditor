package net.dankito.richtexteditor.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase


abstract class InsertNumberedListCommandBase(icon: Icon) : ActiveStateToolbarCommand(CommandName.INSERTORDEREDLIST, icon) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.insertNumberedList()
    }

}