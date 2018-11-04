package net.dankito.richtexteditor.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase
import java.io.File
import java.net.URL


abstract class InsertImageCommandBase(icon: Icon) : ToolbarCommand(CommandName.INSERTIMAGE, icon) {


    abstract protected fun selectImageToInsert(imageSelected: (imageUrl: String, alternateText: String) -> Unit)


    override fun executeCommand(executor: JavaScriptExecutorBase) {
        selectImageToInsert { imageUrl, alternateText ->
            executor.insertImage(imageUrl, alternateText, getImageRotation(imageUrl))
        }
    }


    protected open fun getImageRotation(imageUrl: String): Int {
        try {
            val uri = URL(imageUrl).toURI()
            return getImageRotation(File(uri))
        } catch (ignored: Exception) { } // e. g. a http url cannot be converted to File

        return 0
    }

    protected open fun getImageRotation(localImage: File): Int = 0

}