package net.dankito.richtexteditor.command

import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.JavaScriptExecutorBase


abstract class InsertLinkCommandBase(icon: ImageReference) : ToolbarCommand(CommandName.INSERTLINK, icon) {


    abstract protected fun selectLinkToInsert(linkSelected: (url: String, title: String) -> Unit)


    override fun executeCommand(executor: JavaScriptExecutorBase) {
        selectLinkToInsert { url, title ->
            executor.insertLink(url, title)
        }
    }

}