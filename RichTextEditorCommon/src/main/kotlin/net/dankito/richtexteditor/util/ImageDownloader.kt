package net.dankito.richtexteditor.util

import net.dankito.data_access.network.webclient.OkHttpWebClient
import net.dankito.data_access.network.webclient.RequestParameters
import net.dankito.data_access.network.webclient.ResponseType
import org.slf4j.LoggerFactory
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream


class ImageDownloader {

    companion object {
        private val log = LoggerFactory.getLogger(ImageDownloader::class.java)
    }


    fun downloadImageAsync(imageUrl: String, targetFile: File, callback: (isSuccessful: Boolean) -> Unit) {
        val webClient = OkHttpWebClient()
        val outputStream = BufferedOutputStream(FileOutputStream(targetFile))

        webClient.getAsync(RequestParameters(imageUrl, responseType = ResponseType.Bytes, downloadProgressListener = { _, downloadedChunk ->
            try {
                outputStream.write(downloadedChunk)
            } catch (e: Exception) {
                log.error("Could not write downloaded image chunk of $imageUrl to file $targetFile", e)
                callback(false)
            }
        })) { response ->
            try { outputStream.close() } catch (e: Exception) { log.error("Could not close output stream for file $targetFile", e) }

            callback(response.isSuccessful)
        }
    }

}