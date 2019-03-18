package net.dankito.richtexteditor.command

import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.JavaScriptExecutorBase


abstract class InsertBulletListCommandBase(icon: ImageReference) : ActiveStateToolbarCommand(CommandName.INSERTUNORDEREDLIST, icon) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.insertBulletList()
    }

}