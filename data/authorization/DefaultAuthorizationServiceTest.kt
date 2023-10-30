package com.ygorxkharo.obey.web.data.authorization

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import com.ygorxkharo.obey.web.domain.authentication.model.AuthorizationResult
import com.ygorxkharo.obey.web.domain.common.Result

class DefaultAuthorizationServiceTest {

    private var isMusicPlatformAuthServiceCalled = false
    private val musicPlatformAuthorizationService = object: MusicPlatformAuthorizationService {
        override fun performUserAuthorization(onComplete: (Result<AuthorizationResult>) -> Unit) {
            isMusicPlatformAuthServiceCalled = true
        }
    }

    private val validPlatformName = "test"
    private val musicPlatformAuthServiceCollection = mapOf(validPlatformName to musicPlatformAuthorizationService)
    private lateinit var sut: DefaultAuthorizationService
    private val onCompleteCallback: (Result<AuthorizationResult>) -> Unit = {}

    @BeforeTest
    fun setup() {
        sut = DefaultAuthorizationService(musicPlatformAuthServiceCollection)
        isMusicPlatformAuthServiceCalled = false
    }

    @Test
    fun test_When_AValidMusicPlatformAuthServiceExists_Expect_TheMusicPlatformAuthToGetUserAuthorization() {
        //Arrange
        //Act
        sut.getUserAuthorization(validPlatformName, onCompleteCallback)

        //Assert
        assertTrue(isMusicPlatformAuthServiceCalled)
    }

    @Test
    fun test_When_AnInvalidMusicPlatformAuthServiceExists_Expect_ExceptionToBeThrown() {
        //Arrange
        val expectedErrorMessage = "This authorization provider does not exist"
        val invalidPlatformName = "samsun"

        //Act
        val actualErrorMessage = try {
            val emptyErrorMessage = ""
            sut.getUserAuthorization(invalidPlatformName, onCompleteCallback)
            emptyErrorMessage
        } catch (ex: MusicPlatformAuthorizationException) {
            ex.message
        }

        //Assert
        assertEquals(expectedErrorMessage, actualErrorMessage)
    }
}
