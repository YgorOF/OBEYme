package com.ygorxkharo.obey.web.data.playlists.linking.sdk.applemusic.dto

import com.ygorxkharo.obey.web.data.playlists.integration.MusicPlatformLibraryException
import com.ygorxkharo.obey.web.data.playlists.integration.sdk.applemusic.dto.AppleMusicErrorMapper
import com.ygorxkharo.obey.web.data.playlists.linking.sdk.applemusic.AppleMusicPlaylistTestFixture
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AppleMusicErrorMapperTest {

    @Test
    fun test_When_ErrorBodyIsReceived_Expect_AMusicPlatformExceptionWithDetailMessageToBeReturned() {
        //Arrange
        val sut = AppleMusicErrorMapper()
        val expectedErrorMessage = "No related resources found for tracks"

        //Act
        val actualException = sut.mapToException(errorBody = AppleMusicPlaylistTestFixture.ERROR_DTO_PAYLOAD_JSON_VALUE)

        //Assert
        assertTrue(actualException is MusicPlatformLibraryException)
        assertEquals(expectedErrorMessage, actualException.message)
    }

}