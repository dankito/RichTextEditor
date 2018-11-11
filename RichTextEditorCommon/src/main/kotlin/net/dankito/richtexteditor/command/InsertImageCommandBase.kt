package net.dankito.richtexteditor.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase
import java.io.File
import java.net.URL


abstract class InsertImageCommandBase(icon: Icon) : ToolbarCommand(CommandName.INSERTIMAGE, icon) {


    abstract protected fun selectImageToInsert(imageSelected: (imageUrl: String, alternateText: String) -> Unit)


    override fun executeCommand(executor: JavaScriptExecutorBase) {
        selectImageToInsert { imageUrl, alternateText ->
            executor.insertImage(imageUrl, alternateText, getRotationToShowImageCorrectlyOrientated(imageUrl))
        }
    }


    protected open fun getRotationToShowImageCorrectlyOrientated(imageUrl: String): Int {
        try {
            val uri = URL(imageUrl).toURI()

            val imageRotation = getImageRotation(File(uri))
            when (imageRotation) {
                90 -> return 270 // if the image is rotated by 90 degree it has to be rotated by 270 degree
                270 -> return 90
                else -> return imageRotation // 0 and 180 degree
            }
        } catch (ignored: Exception) { } // e. g. a http url cannot be converted to File

        return 0
    }

    protected open fun getImageRotation(localImage: File): Int = 0

}