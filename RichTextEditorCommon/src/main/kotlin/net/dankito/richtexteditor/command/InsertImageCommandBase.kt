package net.dankito.richtexteditor.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase


abstract class InsertImageCommandBase(icon: Icon) : ToolbarCommand(CommandName.INSERTIMAGE, icon) {


    abstract protected fun selectImageToInsert(imageSelected: (imageUrl: String, alternateText: String) -> Unit)


    override fun executeCommand(executor: JavaScriptExecutorBase) {
        selectImageToInsert { imageUrl, alternateText ->
            executor.insertImage(imageUrl, alternateText)
        }
    }

}