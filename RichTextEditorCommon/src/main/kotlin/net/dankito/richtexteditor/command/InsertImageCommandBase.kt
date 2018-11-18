package net.dankito.richtexteditor.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase
import java.io.File


abstract class InsertImageCommandBase(icon: Icon) : ToolbarCommand(CommandName.INSERTIMAGE, icon) {


    abstract protected fun selectImageToInsert(imageSelected: (imageUrl: String, alternateText: String) -> Unit)


    override fun executeCommand(executor: JavaScriptExecutorBase) {
        selectImageToInsert { imageUrl, alternateText ->
            executor.insertImage(imageUrl, alternateText, getRotationToShowImageCorrectlyOrientated(imageUrl))
        }
    }


    protected open fun getRotationToShowImageCorrectlyOrientated(imageUrl: String): Int {
        try {
            return getImageRotation(File(imageUrl))
        } catch (ignored: Exception) { } // e. g. a http url cannot be converted to File, and we can only get rotation from local files (would have to download remote image otherwise)

        return 0
    }

    protected open fun getImageRotation(localImage: File): Int = 0

}