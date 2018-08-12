package net.dankito.data_access.network.webclient

import java.io.InputStream


data class WebClientResponse(val isSuccessful: Boolean,
                             val responseCode: Int = -1,
                             val headers: Map<String, String>? = null,
                             val error: Exception? = null,
                             val body: String? = null, val responseStream: InputStream? = null) {

    fun getHeaderValue(headerName: String): String? {
        val headerNameLowerCased = headerName.toLowerCase() // header names are case insensitive, so compare them lower cased

        headers?.keys?.forEach {
            if(it.toLowerCase() == headerNameLowerCased) {
                return headers[it]
            }
        }

        return null
    }


    val isInformationalResponse: Boolean
        get() {
            return responseCode >= 100 && responseCode < 200
        }

    val isSuccessResponse: Boolean
        get() {
            return responseCode >= 200 && responseCode < 300
        }

    val isRedirectionResponse: Boolean
        get() {
            return responseCode >= 300 && responseCode < 400
        }

    val isClientErrorResponse: Boolean
        get() {
            return responseCode >= 400 && responseCode < 500
        }

    val isServerErrorResponse: Boolean
        get() {
            return responseCode >= 500 && responseCode < 600
        }

}