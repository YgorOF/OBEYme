package com.ygorxkharo.obey.web.domain.songs.linking.usecases

import com.ygorxkharo.obey.web.data.playlists.integration.MusicPlatformLibraryException
import com.ygorxkharo.obey.web.domain.common.Failure
import kotlin.test.Test
import com.ygorxkharo.obey.web.domain.common.Result
import com.ygorxkharo.obey.web.domain.common.Success
import com.ygorxkharo.obey.web.domain.musicplatforms.songs.linking.MusicPlatformSongLibraryService
import com.ygorxkharo.obey.web.domain.musicplatforms.songs.linking.model.LibrarySongPerformerAttribution
import com.ygorxkharo.obey.web.domain.musicplatforms.songs.linking.model.LibrarySongPlaybackAttribution
import com.ygorxkharo.obey.web.domain.musicplatforms.songs.linking.model.LibrarySongPublishingAttribution
import com.ygorxkharo.obey.web.domain.musicplatforms.songs.linking.model.LibrarySongsRetrievalResult
import com.ygorxkharo.obey.web.domain.musicplatforms.songs.linking.model.MusicPlatformLibrarySong
import com.ygorxkharo.obey.web.domain.musicplatforms.songs.linking.model.PlaylistSongsRetrievalRequest
import com.ygorxkharo.obey.web.domain.musicplatforms.songs.linking.usecases.GetSongsFromPlaylistUseCase
import kotlin.js.Date
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetSongsFromPlaylistUseCaseTest {

    private lateinit var actualResult: Result<LibrarySongsRetrievalResult>
    private val onCompleteCallback: (Result<LibrarySongsRetrievalResult>) -> Unit = {
        actualResult = it
    }
    private val playlistSongRetrievalRequest = PlaylistSongsRetrievalRequest(
        musicPlatformName = "apple_music",
        accessToken = "ey.33sfkjlsfeowjalfjdalfjjaofohas",
        playlistId = "",
        resultLimit = 0
    )

    private val musicPlatformSongs = listOf(
        MusicPlatformLibrarySong(
            songPlaybackAttribution = LibrarySongPlaybackAttribution(
                id = "",
                secondaryId = "",
                durationInMillis = 0L
            ),
            performerAttribution = LibrarySongPerformerAttribution(
                artistName = "",
                songTitle = "",
                albumName = "",
                genres = emptyList()
            ),
            publishingAttribution = LibrarySongPublishingAttribution(
                releaseDateUTC = Date("2020-03-03")
            )
        )
    )

    private val totalSongsCount = 1
    private val nextSongCollectionOffsetIndex = 2

    @Test
    fun test_When_InvokingUseCaseWithSuccess_Expect_SuccessResultToBeReceivedOnComplete() {
        //Arrange
        val expectedLibrarySongRetrievalResult = LibrarySongsRetrievalResult(
            totalCount = totalSongsCount,
            nextSongCollectionOffsetIndex = nextSongCollectionOffsetIndex,
            songs = musicPlatformSongs
        )
        val musicPlatformSongLibraryService = stubMusicPlatformSongLibraryService(librarySongsRetrievalResult =  expectedLibrarySongRetrievalResult)
        val sut = GetSongsFromPlaylistUseCase(musicPlatformSongLibraryService)

        //Act
        sut.invoke(playlistSongRetrievalRequest, onCompleteCallback)

        //Assert
        assertTrue(actualResult is Success)
        assertEquals(expectedLibrarySongRetrievalResult, (actualResult as Success).payload)
    }

    @Test
    fun test_When_InvokingUseCaseWithFailure_Expect_FailureResultToBeReceivedOnComplete() {
        //Arrange
        val expectedErrorMessage = "Unable to get linked playlists"
        val musicPlatformSongLibraryService = stubMusicPlatformSongLibraryService(errorMessage = expectedErrorMessage)
        val sut = GetSongsFromPlaylistUseCase(musicPlatformSongLibraryService)

        //Act
        sut.invoke(playlistSongRetrievalRequest, onCompleteCallback)

        //Assert
        assertTrue(actualResult is Failure)
        assertEquals(expectedErrorMessage, (actualResult as Failure).error.message)
    }

    private fun stubMusicPlatformSongLibraryService(
        librarySongsRetrievalResult: LibrarySongsRetrievalResult? = null,
        errorMessage: String? = null
    ): MusicPlatformSongLibraryService {
        return object: MusicPlatformSongLibraryService {
            override fun getSongsFromPlaylist(
                songsRetrievalRequest: PlaylistSongsRetrievalRequest,
                onComplete: (Result<LibrarySongsRetrievalResult>) -> Unit,
            ) {
                if(librarySongsRetrievalResult != null) {
                    return onComplete.invoke(Success(librarySongsRetrievalResult))
                }

                if(errorMessage != null) {
                    val libraryError = MusicPlatformLibraryException(errorMessage)
                    onComplete(Failure(libraryError))
                }
            }
        }
    }

}