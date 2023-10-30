package com.ygorxkharo.obey.web.data.authorization.sdk.apple

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import com.ygorxkharo.obey.web.data.authorization.testing.AuthRequestHandlerTestFixture
import com.ygorxkharo.obey.web.data.authorization.AuthorizationRequestHandler
import com.ygorxkharo.obey.web.data.authorization.MusicPlatformAuthorizationException
import com.ygorxkharo.obey.web.domain.common.Result
import com.ygorxkharo.obey.web.domain.authentication.model.AuthorizationResult
import com.ygorxkharo.obey.web.domain.common.Failure
import com.ygorxkharo.obey.web.domain.common.Success

class AppleAuthorizationServiceTest {

    private lateinit var requestHandler: AuthorizationRequestHandler<String>
    private lateinit var actualAuthResult: Result<AuthorizationResult>
    private val onComplete: (Result<AuthorizationResult>) -> Unit = {
        actualAuthResult = it
    }

    @Test
    fun test_When_RequestHandlerReturnsResponsePayload_Expect_OnCompleteToContainASuccessResult() {
        //Arrange
        val expectedAccessToken = "akjfhakjfhkahdfjhasdf"
        val responsePayload = "http://example.com/#id_token=$expectedAccessToken"
        requestHandler = AuthRequestHandlerTestFixture.stubAuthRequestHandler(responsePayload = responsePayload)
        val sut = AppleAuthorizationService(requestHandler)

        //Act
        sut.performUserAuthorization(onComplete)

        //Assert
        assertTrue(actualAuthResult is Success)
        val authenticationResult = (actualAuthResult as Success).payload
        assertEquals(expectedAccessToken, authenticationResult.accessToken)
    }

    @Test
    fun test_When_RequestHandlerThrowsAnError_Expect_OnCompleteToContainAFailureResult() {
        //Arrange
        val expectedErrorMessage = "Unable to complete authorization"
        val responseError = MusicPlatformAuthorizationException(expectedErrorMessage)
        requestHandler = AuthRequestHandlerTestFixture.stubAuthRequestHandler(responseError = responseError)
        val sut = AppleAuthorizationService(requestHandler)

        //Act
        sut.performUserAuthorization(onComplete)

        //Assert
        assertTrue(actualAuthResult is Failure)
        val authenticationResult = (actualAuthResult as Failure).error
        assertEquals(expectedErrorMessage, authenticationResult.message)
    }
}