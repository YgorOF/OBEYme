package com.ygorxkharo.obey.web.app.domain.authorization.usecases

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import com.ygorxkharo.obey.web.app.data.testing.authorization.AuthorizationServiceFixture.stubAuthorizationService
import com.ygorxkharo.obey.web.domain.common.Failure
import com.ygorxkharo.obey.web.domain.common.Result
import com.ygorxkharo.obey.web.domain.common.Success
import com.ygorxkharo.obey.web.domain.authentication.model.AuthorizationResult
import com.ygorxkharo.obey.web.domain.authentication.usecases.GetUserAuthorizationUseCase

class GetUserAuthorizationUseCaseTest {

    private lateinit var sut: GetUserAuthorizationUseCase
    private lateinit var actualAuthorizationResult: Result<AuthorizationResult>
    private val onCompleteAuthorization: (Result<AuthorizationResult>) -> Unit = {
        actualAuthorizationResult = it
    }
    private lateinit var actualPlatformNameValue: String
    private val platformNameValueListener: (String) -> Unit = {
        actualPlatformNameValue = it
    }

    @Test
    fun test_When_UserAuthorizationIsAcquired_Expect_TheAuthRepositoryReturnsWithAnAccessToken() {
        //Arrange
        val expectedAccessToken = "ojsdflkj22rafafafaf"
        val expectedPlatformName = "apple"
        val successfulResult = Success(AuthorizationResult(expectedAccessToken))
        val authService = stubAuthorizationService(platformNameValueListener, successfulResult)
        sut = GetUserAuthorizationUseCase(authService)

        //Act
        sut.invoke(expectedPlatformName, onCompleteAuthorization)

        //Assert
        assertTrue(actualAuthorizationResult is Success)
        val actualAuthResult = (actualAuthorizationResult as Success).payload
        assertEquals(expectedAccessToken, actualAuthResult.accessToken)
        assertEquals(expectedPlatformName, actualPlatformNameValue)
    }

    @Test
    fun test_When_UserAuthorizationFails_Expect_TheAuthRepositoryReturnsWithErrorMessage() {
        //Arrange
        val expectedPlatformName = "google"
        val expectedErrorMessage = "Error getting user authorization"
        val failureResult = Failure(Exception(expectedErrorMessage))
        val authService = stubAuthorizationService(platformNameValueListener, failureResult)
        sut = GetUserAuthorizationUseCase(authService)

        //Act
        sut.invoke(expectedPlatformName, onCompleteAuthorization)

        //Assert
        assertTrue(actualAuthorizationResult is Failure)
        val actualAuthResult = (actualAuthorizationResult as Failure).error
        assertEquals(expectedErrorMessage, actualAuthResult.message)
        assertEquals(expectedPlatformName, actualPlatformNameValue)
    }
}