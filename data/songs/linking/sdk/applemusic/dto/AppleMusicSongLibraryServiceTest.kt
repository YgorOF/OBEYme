package com.ygorxkharo.obey.web.data.songs.linking.sdk.applemusic.dto

import com.ygorxkharo.obey.web.data.playlists.integration.MusicPlatformLibraryException
import com.ygorxkharo.obey.web.data.songs.dto.MusicPlatformSongsRequestHandler
import com.ygorxkharo.obey.web.data.songs.linking.MusicPlatformLibrarySongMapper
import com.ygorxkharo.obey.web.data.songs.linking.sdk.applemusic.AppleMusicLibrarySongTestFixture
import com.ygorxkharo.obey.web.data.songs.linking.sdk.applemusic.dto.model.AppleMusicLibrarySongDto
import com.ygorxkharo.obey.web.data.songs.linking.sdk.applemusic.dto.model.LibrarySongCollectionDto
import com.ygorxkharo.obey.web.data.songs.linking.sdk.applemusic.dto.model.LibrarySongsRetrievalRequestDto
import com.ygorxkharo.obey.web.data.utils.JsonStringToObjectDeserializer
import com.ygorxkharo.obey.web.domain.common.Failure
import com.ygorxkharo.obey.web.domain.common.Result
import com.ygorxkharo.obey.web.domain.common.Success
import com.ygorxkharo.obey.web.domain.musicplatforms.songs.linking.model.LibrarySongPerformerAttribution
import com.ygorxkharo.obey.web.domain.musicplatforms.songs.linking.model.LibrarySongPlaybackAttribution
import com.ygorxkharo.obey.web.domain.musicplatforms.songs.linking.model.LibrarySongPublishingAttribution
import com.ygorxkharo.obey.web.domain.musicplatforms.songs.linking.model.LibrarySongsRetrievalResult
import com.ygorxkharo.obey.web.domain.musicplatforms.songs.linking.model.MusicPlatformLibrarySong
import com.ygorxkharo.obey.web.domain.musicplatforms.songs.linking.model.PlaylistSongsRetrievalRequest
import kotlin.js.Date
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AppleMusicSongLibraryServiceTest {

    private val musicPlatformSong = MusicPlatformLibrarySong(
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

    private lateinit var actualLibrarySongRetrievalResult: Result<LibrarySongsRetrievalResult>
    private val onCompleteCallback: (Result<LibrarySongsRetrievalResult>) -> Unit = {
        actualLibrarySongRetrievalResult = it
    }

    private val accessToken = "Rxs.s933jk4ljsafdsa"
    private val playlistId = "p.92jlsflhrrjhrwjfafasdf"
    private val resultLimit = 1
    private val librarySongsRetrievalRequest = PlaylistSongsRetrievalRequest(
        musicPlatformName = "test_music_platform",
        accessToken = accessToken,
        playlistId = playlistId,
        resultLimit = resultLimit
    )

    @Test
    fun test_When_PlaylistSongsRetrievalIsSuccessfulWithNextSongsOffsetUrl_Expect_SuccessAndPositiveNextSongOffsetIndex() {
        //Arrange
        val expectedTotalSongsCount = 1
        val expectedNextSongsOffsetIndex = 2
        val jsonResponsePayload = AppleMusicLibrarySongTestFixture.APPLE_MUSIC_LIBRARY_SONG_JSON_RESPONSE
        val librarySongCollectionDto = JsonStringToObjectDeserializer.convertFromString(
            LibrarySongCollectionDto.serializer(),
            jsonResponsePayload
        )
        val requestHandler = stubRequestHandler(librarySongCollectionDto = librarySongCollectionDto)
        val songsMapper = stubSongsMapper()
        val sut = AppleMusicSongLibraryService(requestHandler, songsMapper)

        //Act
        sut.getSongsFromPlaylist(librarySongsRetrievalRequest, onCompleteCallback)

        //Assert
        assertTrue(actualLibrarySongRetrievalResult is Success)
        val successResult = (actualLibrarySongRetrievalResult as Success).payload

        assertEquals(expectedTotalSongsCount, successResult.totalCount)
        assertEquals(expectedNextSongsOffsetIndex, successResult.nextSongCollectionOffsetIndex)
        assertEquals(musicPlatformSong, successResult.songs.first())
    }

    @Test
    fun test_When_PlaylistSongsRetrievalIsSuccessfulWithoutNextSongsOffsetUrl_Expect_SuccessAndNegativeNextSongOffsetIndex() {
        //Arrange
        val expectedNextSongsOffsetIndex = -1
        var jsonResponsePayload = AppleMusicLibrarySongTestFixture.APPLE_MUSIC_LIBRARY_SONG_JSON_RESPONSE
        jsonResponsePayload = jsonResponsePayload.replace("\"next\": \"/v1/me/library/songs?offset=2\",", "")
        val librarySongCollectionDto = JsonStringToObjectDeserializer.convertFromString(
            LibrarySongCollectionDto.serializer(),
            jsonResponsePayload
        )
        val requestHandler = stubRequestHandler(librarySongCollectionDto = librarySongCollectionDto)
        val songsMapper = stubSongsMapper()
        val sut = AppleMusicSongLibraryService(requestHandler, songsMapper)

        //Act
        sut.getSongsFromPlaylist(librarySongsRetrievalRequest, onCompleteCallback)

        //Assert
        assertTrue(actualLibrarySongRetrievalResult is Success)
        val successResult = (actualLibrarySongRetrievalResult as Success).payload

        assertEquals(expectedNextSongsOffsetIndex, successResult.nextSongCollectionOffsetIndex)
    }

    @Test
    fun test_When_PlaylistSongsRetrievalFails_Expect_FailureWithException() {
        //Arrange
        val expectedErrorMessage = "Error getting songs from playlist with playlist ID: $playlistId"
        val requestHandler = stubRequestHandler(errorMessage = expectedErrorMessage)
        val songsMapper = stubSongsMapper()
        val sut = AppleMusicSongLibraryService(requestHandler, songsMapper)

        //Act
        sut.getSongsFromPlaylist(librarySongsRetrievalRequest, onCompleteCallback)

        //Assert
        assertTrue(actualLibrarySongRetrievalResult is Failure)
        val failureResultError = (actualLibrarySongRetrievalResult as Failure).error
        assertEquals(expectedErrorMessage, failureResultError.message)
    }

    private fun stubRequestHandler(
        librarySongCollectionDto: LibrarySongCollectionDto? = null,
        errorMessage: String? = null
    ): MusicPlatformSongsRequestHandler<LibrarySongsRetrievalRequestDto, LibrarySongCollectionDto> {
        return object: MusicPlatformSongsRequestHandler<LibrarySongsRetrievalRequestDto, LibrarySongCollectionDto> {
            override fun handleSongsRetrievalRequest(
                requestPayload: LibrarySongsRetrievalRequestDto,
                onRequestHandled: (LibrarySongCollectionDto) -> Unit,
                onRequestFailed: (Exception) -> Unit,
            ) {
                if(librarySongCollectionDto != null) {
                    onRequestHandled(librarySongCollectionDto)
                }

                if(errorMessage != null) {
                    onRequestFailed(MusicPlatformLibraryException(errorMessage))
                }
            }
        }
    }

    private fun stubSongsMapper(): MusicPlatformLibrarySongMapper<AppleMusicLibrarySongDto> {
        return object: MusicPlatformLibrarySongMapper<AppleMusicLibrarySongDto> {
            override fun mapToLibrarySong(thirdPartyPlatformSong: AppleMusicLibrarySongDto): MusicPlatformLibrarySong {
                return musicPlatformSong
            }
        }
    }

}