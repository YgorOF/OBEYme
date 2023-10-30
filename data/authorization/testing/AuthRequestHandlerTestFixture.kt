package com.ygorxkharo.obey.web.data.authorization.testing

import com.ygorxkharo.obey.web.data.authorization.AuthorizationRequestHandler

/**
 * Testing fixture tot stub an authorization request handler for various authorization platforms
 */
object AuthRequestHandlerTestFixture {

    /**
     * Stub the outputs of an authorization request handler which returns a response payload of generic type [T]
     *
     * @param responsePayload The response payload in the case the authorization request is successfully processed
     * @param responseError Thrown when an error occurs while processing the authorization request
     * @return an instance of a [AuthorizationRequestHandler] with a response payload of generic type [T]
     */
    fun <T> stubAuthRequestHandler(
        responsePayload: T? = null,
        responseError: Exception? = null
    ): AuthorizationRequestHandler<T> {

        return object: AuthorizationRequestHandler<T> {
            override fun handleRequest(onRequestHandled: (T) -> Unit, onRequestFailed: (Exception) -> Unit) {
                if(responsePayload != null) {
                    onRequestHandled.invoke(responsePayload)
                } else if(responseError != null) {
                    onRequestFailed.invoke(responseError)
                }
            }
        }
    }
}
