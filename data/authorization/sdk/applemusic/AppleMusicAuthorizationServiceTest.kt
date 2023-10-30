package com.ygorxkharo.obey.web.data.authorization.sdk.applemusic

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import com.ygorxkharo.obey.web.data.authorization.MusicPlatformAuthorizationException
import com.ygorxkharo.obey.web.data.authorization.testing.AuthRequestHandlerTestFixture
import com.ygorxkharo.obey.web.domain.authentication.model.AuthorizationResult
import com.ygorxkharo.obey.web.domain.common.Failure
import com.ygorxkharo.obey.web.domain.common.Result
import com.ygorxkharo.obey.web.domain.common.Success

class AppleMusicAuthorizationServiceTest {

    private lateinit var actualAuthorizationResult: Result<AuthorizationResult>
    private val onCompleted: (Result<AuthorizationResult>) -> Unit = {
        actualAuthorizationResult = it
    }

    @Test
    fun test_When_AppleMusicAuthRequestIsSuccessful_Expect_TheAuthorizationResultToBeAcquiredOnCompletion() {
        //Arrange
        val expectedAccessToken = "AF02jflkasjflaksjflajfljadslfajslfkjafadf"
        val requestHandler = AuthRequestHandlerTestFixture.stubAuthRequestHandler(responsePayload = expectedAccessToken)
        val sut = AppleMusicAuthorizationService(requestHandler)

        //Act
        sut.performUserAuthorization(onCompleted)

        //Assert
        assertTrue(actualAuthorizationResult is Success)
        val authResult = (actualAuthorizationResult as Success).payload
        assertEquals(expectedAccessToken, authResult.accessToken)
    }

    @Test
    fun test_When_AppleMusicAuthRequestFails_Expect_AnExceptionToBeAcquiredOnCompletion() {
        //Arrange
        val expectedErrorMessage = "Unable to perform user authorization on Apple Music"
        val errorException = MusicPlatformAuthorizationException(expectedErrorMessage)
        val requestHandler = AuthRequestHandlerTestFixture.stubAuthRequestHandler<String>(responseError = errorException)
        val sut = AppleMusicAuthorizationService(requestHandler)

        //Act
        sut.performUserAuthorization(onCompleted)

        //Assert
        assertTrue(actualAuthorizationResult is Failure)
        val authorizationException = (actualAuthorizationResult as Failure).error
        assertEquals(expectedErrorMessage, authorizationException.message)
    }

}
