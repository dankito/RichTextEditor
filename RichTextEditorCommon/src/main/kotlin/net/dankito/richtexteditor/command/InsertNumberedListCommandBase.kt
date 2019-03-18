package net.dankito.richtexteditor.command

import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.JavaScriptExecutorBase


abstract class InsertNumberedListCommandBase(icon: ImageReference) : ActiveStateToolbarCommand(CommandName.INSERTORDEREDLIST, icon) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.insertNumberedList()
    }

}