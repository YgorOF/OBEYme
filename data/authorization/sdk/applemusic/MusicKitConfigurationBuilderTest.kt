package com.ygorxkharo.obey.web.data.authorization.sdk.applemusic

import com.ygorxkharo.obey.web.domain.authentication.config.model.AuthorizationPlatformConfig
import com.ygorxkharo.obey.web.domain.common.Result
import com.ygorxkharo.obey.web.domain.common.Success
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MusicKitConfigurationBuilderTest {

    private val expectedBuildNumberValue = "1783.2"
    private val expectedApplicationName = "Obey Web App"
    private val expectedAppleDevToken = "eylksjfaljflajfljaslfjaslfjalskjflaksjflasdf"
    private lateinit var actualConfigurationResult: Result<AuthorizationPlatformConfig>
    private val onConfiguredCallback: (Result<AuthorizationPlatformConfig>) -> Unit = {
        actualConfigurationResult = it
    }

    @Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
    @Test
    fun test_When_AccessTokenHasBeenAcquiredSuccessfully_Expect_JSONConfigurationToBeReturnedInOnComplete() {
        //Arrange
        val sut = MusicKitConfigurationBuilder(
            appleMusicDevToken = expectedAppleDevToken,
            applicationName = expectedApplicationName,
            buildNumberValue = expectedBuildNumberValue
        )

        //Act
        sut.build(onConfiguredCallback)

        //Assert
        assertTrue(actualConfigurationResult is Success)
        val result = actualConfigurationResult as Success
        assertEquals(expectedAppleDevToken, result.payload.accessToken)
        assertEquals(expectedBuildNumberValue, result.payload.buildNumberValue)
        assertEquals(expectedApplicationName, result.payload.applicationName)
    }

}