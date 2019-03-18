package net.dankito.richtexteditor.command

import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.JavaScriptExecutorBase


abstract class ItalicCommandBase(icon: ImageReference) : ActiveStateToolbarCommand(CommandName.ITALIC, icon) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.setItalic()
    }

}