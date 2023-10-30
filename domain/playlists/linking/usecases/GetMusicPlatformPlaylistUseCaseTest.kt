package com.ygorxkharo.obey.web.domain.playlists.linking.usecases

import com.ygorxkharo.obey.web.domain.common.Result
import com.ygorxkharo.obey.web.domain.musicplatforms.playlists.linking.MusicPlatformPlaylistService
import com.ygorxkharo.obey.web.domain.musicplatforms.playlists.linking.model.PlaylistRetrievalRequest
import com.ygorxkharo.obey.web.domain.musicplatforms.playlists.linking.model.PlaylistRetrievalResponse
import com.ygorxkharo.obey.web.domain.musicplatforms.playlists.linking.usecases.GetMusicPlatformPlaylistUseCase
import com.ygorxkharo.obey.web.domain.musicplatforms.radiostations.model.RadioStationRetrievalRequest
import com.ygorxkharo.obey.web.domain.musicplatforms.radiostations.model.RadioStationRetrievalResponse
import kotlin.test.Test
import kotlin.test.assertEquals

class GetMusicPlatformPlaylistUseCaseTest {

    private val musicPlatform = "test"
    private val musicPlatformAccessToken = "ey.xvxlkjalj0s9fwijflajflajdfasfasf"
    private lateinit var actualPlaylistRequestRetrieval: PlaylistRetrievalRequest
    private val musicPlatformPlaylistService = object: MusicPlatformPlaylistService {
        override fun getPlaylistsAsync(
            playlistRetrievalRequest: PlaylistRetrievalRequest,
            onComplete: (Result<PlaylistRetrievalResponse>) -> Unit
        ) {
            actualPlaylistRequestRetrieval = playlistRetrievalRequest
        }

        override fun getUserPersonalRadioStationAsync(
            radioStationRetrievalRequest: RadioStationRetrievalRequest,
            onComplete: (Result<RadioStationRetrievalResponse>) -> Unit,
        ) {}

        override fun getUserRecommendations(
            recommendationsRequest: PlaylistRetrievalRequest,
            onComplete: (Result<PlaylistRetrievalResponse>) -> Unit,
        ) {}
    }

    @Test
    fun test_When_TheUseCaseIsInvoked_Expect_TheMusicPlatformPlaylistServiceToBeCalled() {
        //Arrange
        val sut = GetMusicPlatformPlaylistUseCase(musicPlatformPlaylistService)
        val expectedOnCompleteCallback: (Result<PlaylistRetrievalResponse>) -> Unit = {}
        val expectedPlaylistRetrievalRequest = PlaylistRetrievalRequest(
            musicPlatform = musicPlatform,
            musicPlatformAccessToken = musicPlatformAccessToken
        )

        //Act
        sut.invoke(expectedPlaylistRetrievalRequest, expectedOnCompleteCallback)

        //Assert
        assertEquals(expectedPlaylistRetrievalRequest, actualPlaylistRequestRetrieval)
    }
}