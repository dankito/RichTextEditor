package net.dankito.richtexteditor.java.fx.util

import org.slf4j.LoggerFactory
import java.io.*
import kotlin.concurrent.thread


class HtmlEditorExtractor {

    companion object {
        private const val EditorResourceFolderPath = "editor"

        private const val EditorHtmlFilename = "editor.html"
        private const val EditorJavaScripFilename = "rich_text_editor.js"
        private const val StyleCssFilename = "style.css"
        private const val NormalizeCssFilename = "normalize.css"
        private const val PlatformStyleCssFilename = "platform_style.css"

        private val EditorFilenames = listOf(EditorHtmlFilename, EditorJavaScripFilename, StyleCssFilename, NormalizeCssFilename, PlatformStyleCssFilename)

        private val log = LoggerFactory.getLogger(HtmlEditorExtractor::class.java)
    }


    fun extractAsync(destinationDirectory: File, extractionDone: (extractedEditorHtmlFile: File?) -> Unit) {
        thread {
            extractionDone(extract(destinationDirectory))
        }
    }

    fun extract(destination: File): File? {
        val destinationDirectory = destination.absoluteFile

        if(destinationDirectory.exists() == false) {
            destinationDirectory.mkdirs()
        }

        return copyResourceFilesToDestination(destinationDirectory)
    }

    private fun copyResourceFilesToDestination(destinationDirectory: File): File? {
        val classLoader = HtmlEditorExtractor::class.java.classLoader
        var extractedEditorHtmlFile: File? = null

        EditorFilenames.forEach { filename ->
            try {
                val extractedFile = copyResourceFileToDestination(destinationDirectory, filename, classLoader)

                if(filename == EditorHtmlFilename && extractedFile != null) {
                    extractedEditorHtmlFile = extractedFile
                }
            } catch(e: Exception) {
                log.error("Could not copy resource file $filename to destination directory $destinationDirectory", e)
            }
        }

        return extractedEditorHtmlFile
    }

    private fun copyResourceFileToDestination(destinationDirectory: File, filename: String, classLoader: ClassLoader): File? {
        val inputStream = classLoader.getResourceAsStream(EditorResourceFolderPath + "/" + filename)

        if(inputStream != null) { // TODO: what to do if resource couldn't be found?
            val destination = File(destinationDirectory, filename)

            writeToFile(inputStream, destination)

            return destination
        }

        return null
    }

    @Throws(Exception::class)
    private fun writeToFile(inputStream: InputStream, destinationFile: File) {
        var outputStream: OutputStream? = null

        try {
            outputStream = BufferedOutputStream(FileOutputStream(destinationFile))

            inputStream.copyTo(outputStream, 16 * 1024)
        } catch (ex: IOException) {
            log.error("Could not write InputStream to file " + destinationFile.absolutePath, ex)
            throw ex
        } finally {
            try {
                outputStream?.flush()
                outputStream?.close()

                inputStream.close()
            } catch (e: IOException) { }
        }
    }

}