package net.dankito.data_access.network.webclient

import okhttp3.*
import org.slf4j.LoggerFactory
import java.io.IOException
import java.io.InputStream
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class OkHttpWebClient : IWebClient {

    companion object {
        private val FORM_URL_ENCODED_MEDIA_TYPE = MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8")
        private val JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8")

        private val log = LoggerFactory.getLogger(OkHttpWebClient::class.java)
    }


    private val cookieJar = object : CookieJar {

        override fun saveFromResponse(url: HttpUrl?, cookies: MutableList<Cookie>?) {
        }

        override fun loadForRequest(url: HttpUrl?): MutableList<Cookie> {
            return ArrayList()
        }

    }

    // avoid creating several instances, should be singleton
    private val client: OkHttpClient


    init {
        val builder = OkHttpClient.Builder()

        builder.followRedirects(true)
        builder.retryOnConnectionFailure(true)
        builder.connectTimeout(RequestParameters.DEFAULT_CONNECTION_TIMEOUT_MILLIS.toLong(), TimeUnit.MILLISECONDS) // TODO: find a way to set per call
        builder.readTimeout(RequestParameters.DEFAULT_CONNECTION_TIMEOUT_MILLIS.toLong(), TimeUnit.MILLISECONDS)
        builder.writeTimeout(RequestParameters.DEFAULT_CONNECTION_TIMEOUT_MILLIS.toLong(), TimeUnit.MILLISECONDS)
        builder.cookieJar(cookieJar)

        client = builder.build()
    }


    override fun get(parameters: RequestParameters): WebClientResponse {
        try {
            val request = createGetRequest(parameters)

            val response = executeRequest(parameters, request)

            return getResponse(parameters, response)
        } catch (e: Exception) {
            return getRequestFailed(parameters, e)
        }
    }

    override fun getAsync(parameters: RequestParameters, callback: (response: WebClientResponse) -> Unit) {
        try {
            val request = createGetRequest(parameters)

            executeRequestAsync(parameters, request, callback)
        } catch (e: Exception) {
            asyncGetRequestFailed(parameters, e, callback)
        }

    }

    private fun createGetRequest(parameters: RequestParameters): Request {
        val requestBuilder = Request.Builder()

        applyParameters(requestBuilder, parameters)

        return requestBuilder.build()
    }


    override fun post(parameters: RequestParameters): WebClientResponse {
        try {
            val request = createPostRequest(parameters)

            val response = executeRequest(parameters, request)

            return getResponse(parameters, response)
        } catch (e: Exception) {
            return postRequestFailed(parameters, e)
        }

    }

    override fun postAsync(parameters: RequestParameters, callback: (response: WebClientResponse) -> Unit) {
        try {
            val request = createPostRequest(parameters)

            executeRequestAsync(parameters, request, callback)
        } catch (e: Exception) {
            asyncPostRequestFailed(parameters, e, callback)
        }

    }

    private fun createPostRequest(parameters: RequestParameters): Request {
        val requestBuilder = Request.Builder()

        setPostBody(requestBuilder, parameters)

        applyParameters(requestBuilder, parameters)

        return requestBuilder.build()
    }

    private fun setPostBody(requestBuilder: Request.Builder, parameters: RequestParameters) {
        if(parameters.isBodySet()) {
            val mediaType = if (parameters.contentType === ContentType.JSON) JSON_MEDIA_TYPE else FORM_URL_ENCODED_MEDIA_TYPE
            val postBody = RequestBody.create(mediaType, parameters.body)

            requestBuilder.post(postBody)
        }
    }


    override fun head(parameters: RequestParameters): WebClientResponse {
        try {
            val request = createHeadRequest(parameters)

            val response = executeRequest(parameters, request)

            return getResponse(parameters, response)
        } catch (e: Exception) {
            return getRequestFailed(parameters, e)
        }
    }

    override fun headAsync(parameters: RequestParameters, callback: (response: WebClientResponse) -> Unit) {
        try {
            val request = createHeadRequest(parameters)

            executeRequestAsync(parameters, request, callback)
        } catch (e: Exception) {
            asyncGetRequestFailed(parameters, e, callback)
        }

    }

    private fun createHeadRequest(parameters: RequestParameters): Request {
        val requestBuilder = Request.Builder()

        applyParameters(requestBuilder, parameters)

        requestBuilder.head()

        return requestBuilder.build()
    }


    private fun applyParameters(requestBuilder: Request.Builder, parameters: RequestParameters) {
        requestBuilder.url(parameters.url)

        if(parameters.isUserAgentSet()) {
            requestBuilder.header("User-Agent", parameters.userAgent)
        }

        // TODO: re-enable setting connection timeout
//        if(parameters.isConnectionTimeoutSet()) {
//            client.setConnectTimeout(parameters.connectionTimeoutMillis.toLong(), TimeUnit.MILLISECONDS)
//        }
//        else {
//            client.setConnectTimeout(RequestParameters.DEFAULT_CONNECTION_TIMEOUT_MILLIS.toLong(), TimeUnit.MILLISECONDS)
//        }

        // TODO: re-enable setting cookie handling (e.g. via adding com.squareup.okhttp3:okhttp-urlconnection dependency)
        setCookieHandling(parameters)
    }

    private fun setCookieHandling(parameters: RequestParameters) {
//        when(parameters.cookieHandling) {
//            CookieHandling.ACCEPT_ALL, CookieHandling.ACCEPT_ALL_ONLY_FOR_THIS_CALL -> cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
//            CookieHandling.ACCEPT_ORIGINAL_SERVER, CookieHandling.ACCEPT_ORIGINAL_SERVER_ONLY_FOR_THIS_CALL -> cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER)
//            else -> cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_NONE)
//        }
    }

    @Throws(Exception::class)
    private fun executeRequest(parameters: RequestParameters, request: Request): Response {
        val response = client.newCall(request).execute()

//        if(parameters.cookieHandling === CookieHandling.ACCEPT_ALL_ONLY_FOR_THIS_CALL || parameters.cookieHandling === CookieHandling.ACCEPT_ORIGINAL_SERVER_ONLY_FOR_THIS_CALL) {
//            cookieManager.cookieStore.removeAll()
//        }

        if(response.isSuccessful == false && parameters.isCountConnectionRetriesSet()) {
            prepareConnectionRetry(parameters)
            return executeRequest(parameters, request)
        }
        else {
            return response
        }
    }

    private fun executeRequestAsync(parameters: RequestParameters, request: Request, callback: (response: WebClientResponse) -> Unit) {
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                asyncRequestFailed(parameters, request, e, callback)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                callback(getResponse(parameters, response))
            }
        })
    }

    private fun getRequestFailed(parameters: RequestParameters, e: Exception): WebClientResponse {
        if(shouldRetryConnection(parameters, e)) {
            prepareConnectionRetry(parameters)
            return get(parameters)
        }
        else {
            log.error("Could not request url " + parameters.url, e)
            return WebClientResponse(false, error = e)
        }
    }

    private fun asyncGetRequestFailed(parameters: RequestParameters, e: Exception, callback: (response: WebClientResponse) -> Unit) {
        if(shouldRetryConnection(parameters, e)) {
            prepareConnectionRetry(parameters)
            getAsync(parameters, callback)
        }
        else {
            callback(WebClientResponse(false, error = e))
        }
    }

    private fun postRequestFailed(parameters: RequestParameters, e: Exception): WebClientResponse {
        if(shouldRetryConnection(parameters, e)) {
            prepareConnectionRetry(parameters)
            return post(parameters)
        }
        else {
            return WebClientResponse(false, error = e)
        }
    }

    private fun asyncPostRequestFailed(parameters: RequestParameters, e: Exception, callback: (response: WebClientResponse) -> Unit) {
        if(shouldRetryConnection(parameters, e)) {
            prepareConnectionRetry(parameters)
            postAsync(parameters, callback)
        }
        else {
            callback(WebClientResponse(false, error = e))
        }
    }

    private fun asyncRequestFailed(parameters: RequestParameters, request: Request, e: Exception, callback: (response: WebClientResponse) -> Unit) {
        if(shouldRetryConnection(parameters, e)) {
            prepareConnectionRetry(parameters)
            executeRequestAsync(parameters, request, callback)
        }
        else {
            log.error("Failure on Request to " + request.url(), e)
            callback(WebClientResponse(false, error = e))
        }
    }

    private fun prepareConnectionRetry(parameters: RequestParameters) {
        parameters.decrementCountConnectionRetries()
        log.info("Going to retry to connect to " + parameters.url + " (count tries left: " + parameters.countConnectionRetries + ")")
    }

    private fun shouldRetryConnection(parameters: RequestParameters, e: Exception): Boolean {
        return parameters.isCountConnectionRetriesSet() && isConnectionException(e)
    }

    private fun isConnectionException(e: Exception): Boolean {
        val errorMessage = e.message?.toLowerCase() ?: ""
        return errorMessage.contains("timeout") || errorMessage.contains("failed to connect")
    }

    @Throws(IOException::class)
    private fun getResponse(parameters: RequestParameters, response: Response): WebClientResponse {
        val headers = copyHeaders(response)

        if(parameters.responseType == ResponseType.String) {
            return WebClientResponse(true, response.code(), headers, body = response.body()?.string())
        }
        else if(parameters.responseType == ResponseType.Stream) {
            return WebClientResponse(true, response.code(), headers, responseStream = response.body()?.byteStream())
        }
        else {
            return streamBinaryResponse(parameters, response, headers)
        }
    }

    private fun copyHeaders(response: Response): Map<String, String>? {
        val headers = HashMap<String, String>()

        response.headers().names().forEach { name ->
            headers.put(name, response.header(name) ?: "")
        }

        return headers
    }

    private fun streamBinaryResponse(parameters: RequestParameters, response: Response, headers: Map<String, String>?): WebClientResponse {
        var inputStream: InputStream? = null
        try {
            inputStream = response.body()?.byteStream()

            val buffer = ByteArray(parameters.downloadBufferSize)
            var downloaded: Long = 0
            val contentLength = response.body()?.contentLength() ?: 0

            publishProgress(parameters, ByteArray(0), 0L, contentLength)
            while (true) {
                val read = inputStream!!.read(buffer)
                if(read == -1) {
                    break
                }

                downloaded += read.toLong()

                publishProgress(parameters, buffer, downloaded, contentLength, read)

                if(isCancelled(parameters)) {
                    return WebClientResponse(false, response.code(), headers)
                }
            }

            return WebClientResponse(true, response.code(), headers)
        } catch (e: IOException) {
            log.error("Could not download binary Response for Url " + parameters.url, e)
            return WebClientResponse(false, response.code(), headers, e)
        } finally {
            inputStream?.let { try { it.close() } catch (ignored: Exception) { } }
        }
    }

    private fun isCancelled(parameters: RequestParameters): Boolean {
        return false // TODO: implement mechanism to abort download
    }

    private fun publishProgress(parameters: RequestParameters, buffer: ByteArray, downloaded: Long, contentLength: Long, read: Int) {
        var downloadedData = buffer

        if(read < parameters.downloadBufferSize) {
            downloadedData = Arrays.copyOfRange(buffer, 0, read)
        }

        publishProgress(parameters, downloadedData, downloaded, contentLength)
    }

    private fun publishProgress(parameters: RequestParameters, downloadedChunk: ByteArray, currentlyDownloaded: Long, total: Long) {
        val progressListener = parameters.downloadProgressListener

        if(progressListener != null) {
            val progress = if (total <= 0) java.lang.Float.NaN else currentlyDownloaded / total.toFloat()
            progressListener(progress, downloadedChunk)
        }
    }

}