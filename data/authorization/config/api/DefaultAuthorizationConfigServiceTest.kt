package com.ygorxkharo.obey.web.data.authorization.config.api

import com.ygorxkharo.obey.web.data.authorization.MusicPlatformAuthorizationException
import com.ygorxkharo.obey.web.data.authorization.config.DefaultAuthorizationConfigService
import com.ygorxkharo.obey.web.data.authorization.config.api.client.dto.AuthConfigDTO
import com.ygorxkharo.obey.web.data.common.browser.http.HttpRequestHandler
import com.ygorxkharo.obey.web.domain.authentication.config.model.AuthConfigRequest
import com.ygorxkharo.obey.web.domain.authentication.model.AuthorizationResult
import com.ygorxkharo.obey.web.domain.common.Failure
import com.ygorxkharo.obey.web.domain.common.Result
import com.ygorxkharo.obey.web.domain.common.Success
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DefaultAuthorizationConfigServiceTest {

    private lateinit var actualResult: Result<AuthorizationResult>
    private val onCompleteCallback: (Result<AuthorizationResult>) -> Unit = {
        actualResult = it
    }
    private val expectedPlatformName = "test_platform"
    private val authConfigRequest = AuthConfigRequest(platformName = expectedPlatformName)

    @Test
    fun test_When_AccessTokenRetrievedSuccessfully_Expect_SuccessResultToBeSentViaOnCompleteCallback() {
        //Arrange
        val expectedAccessToken = "ey.slfajldjl2kjlkjafldhaklfjhlajafasd"
        val requestHandler = stubRequestHandler(accessToken = expectedAccessToken)
        val sut = DefaultAuthorizationConfigService(requestHandler)

        //Act
        sut.getAccessToken(authConfigRequest, onCompleteCallback)

        //Assert
        assertTrue(actualResult is Success)
        assertEquals(expectedAccessToken, (actualResult as Success).payload.accessToken)
    }

    @Test
    fun test_When_AccessTokenRequestFails_Expect_FailureResultToBeSentViaOnCompleteCallback() {
        //Arrange
        val expectedErrorMessage = "Error getting access token"
        val requestHandler = stubRequestHandler(errorMessage = expectedErrorMessage)
        val sut = DefaultAuthorizationConfigService(requestHandler)

        //Act
        sut.getAccessToken(authConfigRequest, onCompleteCallback)

        //Assert
        assertTrue(actualResult is Failure)
        assertEquals(expectedErrorMessage, (actualResult as Failure).error.message)
    }

    private fun stubRequestHandler(
        accessToken: String? = null,
        errorMessage: String? = null
    ): HttpRequestHandler<AuthConfigRequest, AuthConfigDTO> {
        return object: HttpRequestHandler<AuthConfigRequest, AuthConfigDTO> {
            override fun handleRetrievalRequest(
                requestPayload: AuthConfigRequest,
                onRequestHandled: (AuthConfigDTO) -> Unit,
                onRequestFailed: (Exception) -> Unit,
            ) {
                if(accessToken != null) {
                    return onRequestHandled.invoke(AuthConfigDTO(accessToken))
                }

                if(errorMessage != null) {
                    onRequestFailed.invoke(MusicPlatformAuthorizationException(errorMessage))
                }
            }
        }
    }

}