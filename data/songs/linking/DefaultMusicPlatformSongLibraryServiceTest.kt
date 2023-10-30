package com.ygorxkharo.obey.web.data.songs.linking

import com.ygorxkharo.obey.web.data.playlists.integration.MusicPlatformLibraryException
import com.ygorxkharo.obey.web.domain.common.Result
import com.ygorxkharo.obey.web.domain.musicplatforms.songs.linking.SongLibraryService
import com.ygorxkharo.obey.web.domain.musicplatforms.songs.linking.model.LibrarySongsRetrievalResult
import com.ygorxkharo.obey.web.domain.musicplatforms.songs.linking.model.PlaylistSongsRetrievalRequest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DefaultMusicPlatformSongLibraryServiceTest {

    private var isSongLibraryCalled = false
    private val playlistLibraryService = object: SongLibraryService {
        override fun getSongsFromPlaylist(
            songsRetrievalRequest: PlaylistSongsRetrievalRequest,
            onComplete: (Result<LibrarySongsRetrievalResult>) -> Unit,
        ) {
            isSongLibraryCalled = true
        }
    }

    private val validPlatformName = "test"
    private val invalidPlatformName = "samsun"
    private val musicPlatformAccessToken = "ey.slfjl2jkljflkjsalkfjljl2kjlkjfafas"
    private val playlistLibraryServiceCollection = mapOf(validPlatformName to playlistLibraryService)
    private lateinit var sut: DefaultMusicPlatformSongLibraryService
    private val onCompleteCallback: (Result<LibrarySongsRetrievalResult>) -> Unit = {}

    @BeforeTest
    fun setup() {
        sut = DefaultMusicPlatformSongLibraryService(playlistLibraryServiceCollection)
        isSongLibraryCalled = false
    }

    @Test
    fun test_When_AValidSongLibraryServiceExists_Expect_MusicLibrarySongServiceToBeCalled() {
        //Arrange
        val playlistSongRetrievalRequest = PlaylistSongsRetrievalRequest(
            musicPlatformName = validPlatformName,
            accessToken = musicPlatformAccessToken,
            playlistId = "",
            resultLimit = 0
        )
        //Act
        sut.getSongsFromPlaylist(playlistSongRetrievalRequest, onCompleteCallback)

        //Assert
        assertTrue(isSongLibraryCalled)
    }

    @Test
    fun test_When_AnInvalidMusicPlatformNameIsProvided_Expect_ExceptionToBeThrown() {
        //Arrange
        val expectedErrorMessage = "No song library exists for this music platform"
        val playlistSongRetrievalRequest = PlaylistSongsRetrievalRequest(
            musicPlatformName = invalidPlatformName,
            accessToken = musicPlatformAccessToken,
            playlistId = "",
            resultLimit = 0
        )

        //Act
        val actualErrorMessage = try {
            val emptyErrorMessage = ""
            sut.getSongsFromPlaylist(playlistSongRetrievalRequest, onCompleteCallback)
            emptyErrorMessage
        } catch (ex: MusicPlatformLibraryException) {
            ex.message
        }

        //Assert
        assertEquals(expectedErrorMessage, actualErrorMessage)
    }

}