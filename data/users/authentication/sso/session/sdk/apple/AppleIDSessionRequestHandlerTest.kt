package com.ygorxkharo.obey.web.data.users.authentication.sso.session.sdk.apple

import com.ygorxkharo.obey.web.data.users.authentication.sso.session.model.SSOSessionRequest
import com.ygorxkharo.obey.web.data.utils.JWTDecoder
import kotlin.js.Json
import kotlin.js.json
import kotlin.test.Test
import kotlin.test.assertEquals

class AppleIDSessionRequestHandlerTest {

    private val accessToken = "ey.sldfjaljflkwjlk2jlksjfksjfafdljfa"
    private val ssoSessionRequest = SSOSessionRequest(ssoAccessToken = accessToken)

    private lateinit var actualUserEmail: String
    private val onRequestHandled: (String) -> Unit = {
        actualUserEmail = it
    }

    private lateinit var actualErrorException: Exception
    private val onRequestFailed: (Exception) -> Unit = {
        actualErrorException = it
    }

    @Test
    fun test_When_SSOSessionRequestIsSuccessful_Expect_OnRequestHandledToBeTriggeredWithUserEmail() {
        //Arrange
        val expectedUserEmail = "test@email.com"
        val requestHandler = stubJWTDecoder(emailValue = expectedUserEmail, null)
        val sut = AppleIDSessionRequestHandler(requestHandler)

        //Act
        sut.handleRequest(ssoSessionRequest, onRequestHandled, onRequestFailed)

        //Assert
        assertEquals(expectedUserEmail, actualUserEmail)
    }

    @Test
    fun test_When_SSOSessionRequestFails_Expect_OnRequestFailedToBeTriggeredWithAnException() {
        //Arrange
        val expectedExceptionError = "Failed to get user's session details"
        val requestHandler = stubJWTDecoder(emailValue = null, errorMessage = expectedExceptionError)
        val sut = AppleIDSessionRequestHandler(requestHandler)

        //Act
        sut.handleRequest(ssoSessionRequest, onRequestHandled, onRequestFailed)

        //Assert
        assertEquals(expectedExceptionError, actualErrorException.message)
    }

    @Test
    fun test_When_SSOSessionRequestFailsWithNoErrorMessage_Expect_OnRequestFailedToBeTriggeredWithAnExceptionWithDefaultMessage() {
        //Arrange
        val expectedExceptionError = "Error occurred while fetching user session"
        val requestHandler = stubJWTDecoder(emailValue = null, errorMessage = null)
        val sut = AppleIDSessionRequestHandler(requestHandler)

        //Act
        sut.handleRequest(ssoSessionRequest, onRequestHandled, onRequestFailed)

        //Assert
        assertEquals(expectedExceptionError, actualErrorException.message)
    }

    private fun stubJWTDecoder(emailValue: String?, errorMessage: String?): JWTDecoder<Json> {
        val decodedJWTJson = emailValue?.let { email ->
            json(
                Pair("email", email)
            )
        } ?: json()

        return object: JWTDecoder<Json> {
            override fun decode(jwtToken: String): Json {
                if(emailValue != null) return decodedJWTJson
                throw IllegalArgumentException(errorMessage)
            }
        }
    }
}