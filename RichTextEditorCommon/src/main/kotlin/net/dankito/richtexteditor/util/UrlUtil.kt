package net.dankito.richtexteditor.util

import java.net.URI


class UrlUtil {

    /**
     * Returns true if parameter is a valid uri and scheme is either 'http' or 'https'.
     */
    fun isHttpUri(string: String): Boolean {
        try {
            val uri = URI.create(string)
            return uri != null && (uri.scheme.equals("http", true) || uri.scheme.equals("https", true))
        } catch(ignored: Exception) { } // ok, sharedText is not an Uri

        return false
    }

    fun getFileName(url: String): String {
        try {
            val uri = URI(url)
            val path = uri.path

            return path.substringAfterLast('/')
        } catch(e: Exception) { }

        return url.substringAfterLast('/').substringBefore('?')
    }

}