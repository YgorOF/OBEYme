package com.ygorxkharo.obey.web.data.authorization.config.api

import com.ygorxkharo.obey.web.data.authorization.MusicPlatformAuthorizationException
import com.ygorxkharo.obey.web.data.authorization.config.api.client.dto.AuthConfigDTO
import com.ygorxkharo.obey.web.data.common.browser.http.HttpClient
import com.ygorxkharo.obey.web.domain.authentication.config.model.AuthConfigRequest
import com.ygorxkharo.obey.web.domain.common.Failure
import com.ygorxkharo.obey.web.domain.common.Result
import com.ygorxkharo.obey.web.domain.common.Success
import org.w3c.fetch.RequestInit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AuthConfigRequestHandlerTest {

    private lateinit var actualRequestOptions: RequestInit
    private lateinit var actualDestinationUrl: String
    private lateinit var actualAuthConfigDTO: AuthConfigDTO
    private val onRequestHandledCallback:(AuthConfigDTO) -> Unit = {
        actualAuthConfigDTO = it
    }
    private lateinit var actualRequestException: Exception
    private val onRequestFailedCallback: (Exception) -> Unit = {
        actualRequestException = it
    }
    private val expectedPlatformName = "test_platform"
    private val authConfigRequest = AuthConfigRequest(platformName = expectedPlatformName)

    @Test
    fun test_When_AccessTokenRetrievalRequest_Expect_DestinationURLToBeSet() {
        //Arrange
        val expectedRequestMethod = "GET"
        val expectedPlatformNameQueryParameter = "?platform_name=$expectedPlatformName"
        val httpClient = stubHttpClient()
        val sut = AuthConfigRequestHandler(httpClient)

        //Act
        sut.handleRetrievalRequest(authConfigRequest, onRequestHandledCallback, onRequestFailedCallback)

        //Assert
        assertTrue(actualDestinationUrl.endsWith(expectedPlatformNameQueryParameter))
        assertEquals(expectedRequestMethod, actualRequestOptions.method)
    }

    @Test
    fun test_When_RequestIsSuccessful_Expect_onRequestHandledTriggeredWithSuccess() {
        //Arrange
        val expectedAccessToken = "ey.sfjahfahkfhakhaflhkfa"
        val accessTokenResponse = """
            {
                "accessToken": $expectedAccessToken
            }
        """.trimIndent()

        val httpClient = stubHttpClient(authConfigPayload = accessTokenResponse)
        val sut = AuthConfigRequestHandler(httpClient)

        //Act
        sut.handleRetrievalRequest(authConfigRequest, onRequestHandledCallback, onRequestFailedCallback)

        //Assert
        assertEquals(expectedAccessToken, actualAuthConfigDTO.accessToken)
    }

    @Test
    fun test_When_RequestFails_Expect_onRequestFailedToTriggeredWithFailure() {
        //Arrange
        val expectedErrorMessage = "Unable to get access token from backend"
        val httpClient = stubHttpClient(errorMessage = expectedErrorMessage)
        val sut = AuthConfigRequestHandler(httpClient)

        //Act
        sut.handleRetrievalRequest(authConfigRequest, onRequestHandledCallback, onRequestFailedCallback)

        //Assert
        assertEquals(expectedErrorMessage, actualRequestException.message)
    }

    private fun stubHttpClient(
        authConfigPayload: String? = null,
        errorMessage: String? = null
    ): HttpClient<RequestInit, String> {
        return object: HttpClient<RequestInit, String> {
            override fun handleRequest(
                destinationUrl: String,
                requestOptions: RequestInit,
                onRequestHandled: (Result<String>) -> Unit,
            ) {
                actualDestinationUrl = destinationUrl
                actualRequestOptions = requestOptions

                if(authConfigPayload != null) {
                    onRequestHandled(Success(authConfigPayload))
                }

                if(errorMessage != null) {
                    val failureResult = Failure(MusicPlatformAuthorizationException(errorMessage))
                    onRequestHandled(failureResult)
                }
            }
        }
    }

}