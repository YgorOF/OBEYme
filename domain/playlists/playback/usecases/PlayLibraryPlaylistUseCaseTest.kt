package com.ygorxkharo.obey.web.domain.playlists.playback.usecases

import com.ygorxkharo.obey.web.domain.musicplatforms.playlists.playback.MusicPlayerService
import com.ygorxkharo.obey.web.domain.musicplatforms.playlists.playback.model.MusicPlaybackItem
import com.ygorxkharo.obey.web.domain.musicplatforms.playlists.playback.model.MusicLibraryPlaybackRequest
import com.ygorxkharo.obey.web.domain.musicplatforms.playlists.playback.usecases.PlayLibraryPlaylistUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class PlayLibraryPlaylistUseCaseTest {

    private lateinit var actualPlaybackRequest: MusicLibraryPlaybackRequest
    private val musicPlayerService = object: MusicPlayerService {
        override fun startMusicPlayback(playbackRequest: MusicLibraryPlaybackRequest) {
            actualPlaybackRequest = playbackRequest
        }

        override fun loadMusicQueue(playbackRequest: MusicLibraryPlaybackRequest) {}
    }

    @Test
    fun test_When_StartingMusicPlayback_Expect_TheMusicPlayerServiceToBeCalled() {
        //Arrange
        val expectedPlaybackRequest = MusicLibraryPlaybackRequest(
            musicPlatformName = "test",
            playbackItemType = MusicPlaybackItem.PLAYLIST,
            itemId = "p.3difjojfoafadfag"
        )
        val sut = PlayLibraryPlaylistUseCase(musicPlayerService)

        //Act
        sut.invoke(expectedPlaybackRequest)

        //Assert
        assertEquals(expectedPlaybackRequest, actualPlaybackRequest)
    }
}