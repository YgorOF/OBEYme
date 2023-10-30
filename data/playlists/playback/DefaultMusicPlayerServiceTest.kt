package com.ygorxkharo.obey.web.data.playlists.playback

import com.ygorxkharo.obey.web.domain.musicplatforms.playlists.playback.model.MusicLibraryPlaybackRequest
import com.ygorxkharo.obey.web.domain.musicplatforms.playlists.playback.model.MusicPlaybackItem
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultMusicPlayerServiceTest {

    private lateinit var actualPlaybackRequest: MusicLibraryPlaybackRequest
    private val musicPlatformPlayerService = object: MusicPlatformPlayerService {
        override fun playLibraryMedia(playbackRequest: MusicLibraryPlaybackRequest) {
            actualPlaybackRequest = playbackRequest
        }

        override fun loadLibraryMedia(playbackRequest: MusicLibraryPlaybackRequest) {}
    }

    private val validPlatformName = "apple_music"
    private val invalidPlatformName = "samsun"

    private val musicPlatformPlayerServices = mapOf(validPlatformName to musicPlatformPlayerService)
    private val sut = DefaultMusicPlayerService(musicPlatformPlayerServices)

    @Test
    fun test_When_AMusicPlatformPlayerServiceExists_Expect_TheMusicPlatformPlayerServiceToBeCalled() {
        //Arrange
        val expectedPlaybackRequest = MusicLibraryPlaybackRequest(
            musicPlatformName = validPlatformName,
            playbackItemType = MusicPlaybackItem.PLAYLIST,
            itemId = "p.yeklsfalfdjalkfdjaa"
        )
        //Act
        sut.startMusicPlayback(expectedPlaybackRequest)

        //Assert
        assertEquals(expectedPlaybackRequest, actualPlaybackRequest)
    }

    @Test
    fun test_When_AMusicPlatformPlayerServiceDoesNotExists_Expect_AnExceptionToBeThrown() {
        //Arrange
        val expectedErrorMessage = "No music player exists for this music platform"
        val playbackRequest = MusicLibraryPlaybackRequest(
            musicPlatformName = invalidPlatformName,
            playbackItemType = MusicPlaybackItem.PLAYLIST,
            itemId = "p.yeklsfalfdjalkfdjaa"
        )

        //Act
        val actualErrorMessage = try {
            sut.startMusicPlayback(playbackRequest)
            ""
        } catch (ex: MusicPlatformPlayerException) {
            ex.message
        }

        //Assert
        assertEquals(expectedErrorMessage, actualErrorMessage)
    }
}