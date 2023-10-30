package com.ygorxkharo.obey.web.data.users.authentication.sso.session.sdk.apple

import com.ygorxkharo.obey.web.data.users.authentication.UserAuthenticationException
import com.ygorxkharo.obey.web.data.users.authentication.sso.session.SSOSessionRequestHandler
import com.ygorxkharo.obey.web.data.users.authentication.sso.session.model.SSOAccountSessionResult
import com.ygorxkharo.obey.web.data.users.authentication.sso.session.model.SSOSessionRequest
import com.ygorxkharo.obey.web.domain.common.Failure
import com.ygorxkharo.obey.web.domain.common.Result
import com.ygorxkharo.obey.web.domain.common.Success
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AppleIDUserAccountServiceTest {

    private lateinit var actualRequestHandlerResult: Result<SSOAccountSessionResult>
    private val onCompleted: (Result<SSOAccountSessionResult>) -> Unit = {
        actualRequestHandlerResult = it
    }

    private val accessToken = "ey.slfjl2jljlkfajlkfjalkjflj2ljfljlkajf"
    private val ssoSessionRequest = SSOSessionRequest(
        ssoAccessToken = accessToken
    )

    @Test
    fun test_When_SessionRequestIsSuccessful_Expect_TheOnCompleteCallbackToBeTriggeredWithASuccessResult() {
        //Arrange
        val expectedAppleIdValue = "test@apple"
        val requestHandler = stubRequestHandler(responsePayload = expectedAppleIdValue, responseErrorMessage = null)
        val sut = AppleIDUserAccountService(requestHandler)

        //Act
        sut.getCurrentUserSession(ssoSessionRequest, onCompleted)

        //Assert
        assertTrue(actualRequestHandlerResult is Success)
        val successResult = actualRequestHandlerResult as Success
        assertEquals(expectedAppleIdValue, successResult.payload.username)
    }

    @Test
    fun test_When_SessionRequestFails_Expect_TheOnCompleteCallbackToBeTriggeredWithAFailureResult() {
        //Arrange
        val expectedErrorMessage = "Error getting user session details"
        val requestHandler = stubRequestHandler(responsePayload = null, responseErrorMessage = expectedErrorMessage)
        val sut = AppleIDUserAccountService(requestHandler)

        //Act
        sut.getCurrentUserSession(ssoSessionRequest, onCompleted)

        //Assert
        assertTrue(actualRequestHandlerResult is Failure)
        val failureResult = actualRequestHandlerResult as Failure
        assertEquals(expectedErrorMessage, failureResult.error.message)
    }

    private fun stubRequestHandler(
        responsePayload: String?,
        responseErrorMessage: String?
    ): SSOSessionRequestHandler<String> {
        return object: SSOSessionRequestHandler<String> {

            override fun handleRequest(
                ssoSessionRequest: SSOSessionRequest,
                onRequestHandled: (String) -> Unit,
                onRequestFailed: (Exception) -> Unit
            ) {
                if(responsePayload != null) {
                    onRequestHandled.invoke(responsePayload)
                }

                if(responseErrorMessage != null) {
                    onRequestFailed.invoke(UserAuthenticationException(responseErrorMessage))
                }
            }
        }
    }

}