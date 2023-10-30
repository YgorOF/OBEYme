package com.ygorxkharo.obey.web.data.common

import com.ygorxkharo.obey.web.data.common.browser.http.HttpClient
import com.ygorxkharo.obey.web.domain.common.Failure
import com.ygorxkharo.obey.web.domain.common.Result
import com.ygorxkharo.obey.web.domain.common.Success
import org.w3c.fetch.RequestInit


fun mockHttpClient(
    onCaptureRequest: ((String, RequestInit) -> Unit)? = null,
    responseAsString: String? = null,
    errorMessage: String? = null
): HttpClient<RequestInit, String> {

    return object: HttpClient<RequestInit, String> {
        override fun handleRequest(
            destinationUrl: String,
            requestOptions: RequestInit,
            onRequestHandled: (Result<String>) -> Unit,
        ) {
            onCaptureRequest?.invoke(destinationUrl, requestOptions)

            if(responseAsString != null) {
                return onRequestHandled.invoke(Success(responseAsString))
            }

            if(errorMessage != null) {
                val errorException = Exception(errorMessage)
                return onRequestHandled.invoke(Failure(errorException))
            }
        }
    }
}