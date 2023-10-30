package com.ygorxkharo.obey.web.domain.authentication.config.usecases

import com.ygorxkharo.obey.web.data.authorization.MusicPlatformAuthorizationException
import com.ygorxkharo.obey.web.domain.authentication.config.AuthorizationConfigService
import com.ygorxkharo.obey.web.domain.authentication.config.model.AuthConfigRequest
import com.ygorxkharo.obey.web.domain.authentication.model.AuthorizationResult
import com.ygorxkharo.obey.web.domain.common.Failure
import com.ygorxkharo.obey.web.domain.common.Result
import com.ygorxkharo.obey.web.domain.common.Success
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetAuthorizationTokenUseCaseTest {

    private val expectedPlatformName = "platform_name"
    private val authConfigRequest = AuthConfigRequest(platformName = expectedPlatformName)
    private lateinit var actualAuthResult: Result<AuthorizationResult>
    private val onCompleteCallback: (Result<AuthorizationResult>) -> Unit = {
        actualAuthResult = it
    }
    private lateinit var actualAuthConfigRequest: AuthConfigRequest

    @Test
    fun test_When_PerformingAccessTokenRequest_Expect_PlatformNameToBeTheSameSentToService() {
        //Arrange
        val authConfigService = stubAuthConfigService()
        val sut = GetAuthorizationTokenUseCase(authConfigService)

        //Act
        sut.invoke(authConfigRequest, onCompleteCallback)

        //Assert
        assertEquals(expectedPlatformName, actualAuthConfigRequest.platformName)
    }

    @Test
    fun test_When_AccessTokenRetrievedSuccessfully_Expect_SuccessResultToBeSentViaOnCompleteCallback() {
        //Arrange
        val expectedAccessToken = "ey.skfjhakfhakfhkdfhk33hkhkahfhdfa"
        val authConfigService = stubAuthConfigService(accessToken = expectedAccessToken)
        val sut = GetAuthorizationTokenUseCase(authConfigService)

        //Act
        sut.invoke(authConfigRequest, onCompleteCallback)

        //Assert
        assertTrue(actualAuthResult is Success)
        assertEquals(expectedAccessToken, (actualAuthResult as Success).payload.accessToken)
    }

    @Test
    fun test_When_AccessTokenRequestFails_Expect_FailureResultToBeSentViaOnCompleteCallback() {
        //Arrange
        val expectedErrorMessage = "Error getting access token"
        val authConfigService = stubAuthConfigService(errorMessage = expectedErrorMessage)
        val sut = GetAuthorizationTokenUseCase(authConfigService)

        //Act
        sut.invoke(authConfigRequest, onCompleteCallback)

        //Assert
        assertTrue(actualAuthResult is Failure)
        assertEquals(expectedErrorMessage, (actualAuthResult as Failure).error.message)
    }

    private fun stubAuthConfigService(
        accessToken: String? = null,
        errorMessage: String? = null
    ): AuthorizationConfigService {
        return object: AuthorizationConfigService {
            override fun getAccessToken(
                authConfigRequest: AuthConfigRequest,
                onComplete: (Result<AuthorizationResult>) -> Unit,
            ) {
                actualAuthConfigRequest = authConfigRequest

                if(accessToken != null) {
                    val authorizationResult = AuthorizationResult(accessToken = accessToken)
                    return onComplete.invoke(Success(authorizationResult))
                }

                if(errorMessage != null) {
                    val exception = MusicPlatformAuthorizationException(errorMessage)
                    onComplete.invoke(Failure(exception))
                }
            }
        }
    }

}