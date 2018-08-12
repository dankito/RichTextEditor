package net.dankito.data_access.network.webclient


data class RequestParameters(val url: String, var body: String? = null,
                             var contentType: ContentType = ContentType.FORM_URL_ENCODED,
                             var userAgent: String? = RequestParameters.DEFAULT_USER_AGENT,
                            // TODO: re-enable setting cookie handling (e.g. via adding com.squareup.okhttp3:okhttp-urlconnection dependency)
//                             var cookieHandling: CookieHandling = CookieHandling.ACCEPT_NONE,
                            // TODO: re-enable setting connection timeout
//                             var connectionTimeoutMillis: Int = RequestParameters.DEFAULT_CONNECTION_TIMEOUT_MILLIS,
                             var countConnectionRetries: Int = RequestParameters.DEFAULT_COUNT_CONNECTION_RETRIES,
                             var responseType: ResponseType = ResponseType.String,
                             var downloadBufferSize: Int = RequestParameters.DEFAULT_DOWNLOAD_BUFFER_SIZE,
                             var downloadProgressListener: ((progress: Float, downloadedChunk: ByteArray) -> Unit)? = null) {

    companion object {
        const val DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; rv:55.0) Gecko/20100101 Firefox/55.0"

        const val DEFAULT_MOBILE_USER_AGENT = "Mozilla/5.0 (Linux; Android 4.0.4; Galaxy Nexus Build/IMM76B) AppleWebKit/537.36 (KHTML, like Gecko) CChrome/60.0.3112.105 Safari/537.36"

        const val DEFAULT_CONNECTION_TIMEOUT_MILLIS = 2000

        const val DEFAULT_DOWNLOAD_BUFFER_SIZE = 8 * 1024

        const val DEFAULT_COUNT_CONNECTION_RETRIES = 2
    }


    fun isBodySet(): Boolean {
        return !body.isNullOrBlank()
    }

    fun isUserAgentSet(): Boolean {
        return !userAgent.isNullOrBlank()
    }

//    fun isConnectionTimeoutSet(): Boolean {
//        return connectionTimeoutMillis > 0
//    }

    fun isCountConnectionRetriesSet(): Boolean {
        return countConnectionRetries > 0
    }

    fun decrementCountConnectionRetries() {
        this.countConnectionRetries--
    }

}