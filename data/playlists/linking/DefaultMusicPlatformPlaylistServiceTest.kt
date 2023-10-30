package com.ygorxkharo.obey.web.data.playlists.linking

import com.ygorxkharo.obey.web.data.playlists.integration.DefaultMusicPlatformPlaylistService
import com.ygorxkharo.obey.web.data.playlists.integration.MusicPlatformLibraryException
import com.ygorxkharo.obey.web.data.playlists.integration.PlaylistLibraryService
import com.ygorxkharo.obey.web.domain.common.Failure
import com.ygorxkharo.obey.web.domain.musicplatforms.playlists.linking.model.PlaylistRetrievalResponse
import com.ygorxkharo.obey.web.domain.common.Result
import com.ygorxkharo.obey.web.domain.common.Success
import com.ygorxkharo.obey.web.domain.musicplatforms.playlists.linking.model.PlaylistRetrievalRequest
import com.ygorxkharo.obey.web.domain.musicplatforms.radiostations.model.MusicPlatformRadioStation
import com.ygorxkharo.obey.web.domain.musicplatforms.radiostations.model.RadioStationRetrievalRequest
import com.ygorxkharo.obey.web.domain.musicplatforms.radiostations.model.RadioStationRetrievalResponse
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DefaultMusicPlatformPlaylistServiceTest {

    private var isPlaylistLibraryCalled = false
    private var actualRadioStationResult: Result<RadioStationRetrievalResponse>? = null
    private var expectedRadioStation = MusicPlatformRadioStation(
        id = "slfajldfhalfh",
        title = "test Radio",
        coverArtImageUrl = "https://example.ppng"
    )

    private val validPlatformName = "test"
    private val invalidPlatformName = "samsun"
    private val musicPlatformAccessToken = "ey.slfjl2jkljflkjsalkfjljl2kjlkjfafas"
    private val onCompleteCallback: (Result<PlaylistRetrievalResponse>) -> Unit = {}
    private val countryCode = "za"
    private val radioStationRetrievalRequest = RadioStationRetrievalRequest(
        musicPlatform = validPlatformName,
        musicPlatformAccessToken = musicPlatformAccessToken,
        countryCode = countryCode
    )
    private val onRadioStationResultCallback: (Result<RadioStationRetrievalResponse>) -> Unit = {
        actualRadioStationResult = it
    }

    @BeforeTest
    fun setup() {
        isPlaylistLibraryCalled = false
        actualRadioStationResult = null
    }

    @Test
    fun test_When_AValidPlaylistLibraryServiceExists_Expect_MusicPlaylistServiceToGePlaylists() {
        //Arrange
        val playlistLibraryService = stubPlaylistService()
        val playlistLibraryServiceCollection = mapOf(validPlatformName to playlistLibraryService)
        val sut = DefaultMusicPlatformPlaylistService(playlistLibraryServiceCollection)

        val playlistRetrievalRequest = PlaylistRetrievalRequest(
            musicPlatform = validPlatformName,
            musicPlatformAccessToken = musicPlatformAccessToken
        )

        //Act
        sut.getPlaylistsAsync(playlistRetrievalRequest, onCompleteCallback)

        //Assert
        assertTrue(isPlaylistLibraryCalled)
    }

    @Test
    fun test_When_AnInvalidMusicPlatformNameIsProvided_Expect_ExceptionToBeThrown() {
        //Arrange
        val playlistLibraryService = stubPlaylistService()
        val playlistLibraryServiceCollection = mapOf(validPlatformName to playlistLibraryService)
        val sut = DefaultMusicPlatformPlaylistService(playlistLibraryServiceCollection)

        val expectedErrorMessage = "No playlist library exists for this music platform"
        val playlistRetrievalRequest = PlaylistRetrievalRequest(
            musicPlatform = invalidPlatformName,
            musicPlatformAccessToken = musicPlatformAccessToken
        )

        //Act
        val actualErrorMessage = try {
            val emptyErrorMessage = ""
            sut.getPlaylistsAsync(playlistRetrievalRequest, onCompleteCallback)
            emptyErrorMessage
        } catch (ex: MusicPlatformLibraryException) {
            ex.message
        }

        //Assert
        assertEquals(expectedErrorMessage, actualErrorMessage)
    }

    @Test
    fun test_WhenSuccessResultIsReceivedForUserRadioStation_Expect_OnCompleteToReceiveSuccessResult() {
        //Arrange
        val playlistLibraryService = stubPlaylistService(radioStation = expectedRadioStation)
        val playlistLibraryServiceCollection = mapOf(validPlatformName to playlistLibraryService)
        val sut = DefaultMusicPlatformPlaylistService(playlistLibraryServiceCollection)

        //Act
        sut.getUserPersonalRadioStationAsync(radioStationRetrievalRequest, onRadioStationResultCallback)

        //Assert
        assertTrue(actualRadioStationResult is Success)
        assertEquals(expectedRadioStation, (actualRadioStationResult as Success).payload.radioStation)
    }

    @Test
    fun test_WhenFailureResultIsReceivedForUserRadioStation_Expect_OnCompleteToReceiveFailureResult() {
        //Arrange
        val expectedErrorMessage = "Error getting radio stations"
        val playlistLibraryService = stubPlaylistService(radioStationErrorMessage = expectedErrorMessage)
        val playlistLibraryServiceCollection = mapOf(validPlatformName to playlistLibraryService)
        val sut = DefaultMusicPlatformPlaylistService(playlistLibraryServiceCollection)

        //Act
        sut.getUserPersonalRadioStationAsync(radioStationRetrievalRequest, onRadioStationResultCallback)

        //Assert
        assertTrue(actualRadioStationResult is Failure)
        assertEquals(expectedErrorMessage, (actualRadioStationResult as Failure).error.message)
    }

    @Test
    fun test_When_AnInvalidMusicPlatformNameIsProvidedToFetchRadioStation_Expect_ExceptionToBeThrown() {
        //Arrange
        val playlistLibraryService = stubPlaylistService()
        val playlistLibraryServiceCollection = mapOf(invalidPlatformName to playlistLibraryService)
        val sut = DefaultMusicPlatformPlaylistService(playlistLibraryServiceCollection)
        val expectedErrorMessage = "No playlist library exists for this music platform"

        //Act
        val actualErrorMessage = try {
            val emptyErrorMessage = ""
            sut.getUserPersonalRadioStationAsync(radioStationRetrievalRequest, onRadioStationResultCallback)
            emptyErrorMessage
        } catch (ex: MusicPlatformLibraryException) {
            ex.message
        }

        //Assert
        assertEquals(expectedErrorMessage, actualErrorMessage)
    }

    private fun stubPlaylistService(
        radioStation: MusicPlatformRadioStation? = null,
        radioStationErrorMessage: String? = null
    ): PlaylistLibraryService {
        return object: PlaylistLibraryService {
            override fun getPlaylists(
                playlistRetrievalRequest: PlaylistRetrievalRequest,
                onComplete: (Result<PlaylistRetrievalResponse>) -> Unit
            ) {
                isPlaylistLibraryCalled = true
            }

            override fun getUserPersonalRadioStation(
                radioStationRetrievalRequest: RadioStationRetrievalRequest,
                onComplete: (Result<RadioStationRetrievalResponse>) -> Unit,
            ) {

                if(radioStation != null) {
                    val radioStationRetrievalResponse = RadioStationRetrievalResponse(radioStation)
                    return onComplete.invoke(Success(radioStationRetrievalResponse))
                }

                if(radioStationErrorMessage != null) {
                    onComplete.invoke(Failure(MusicPlatformLibraryException(radioStationErrorMessage)))
                }
            }

            override fun getUserRecommendations(
                recommendationsRequest: PlaylistRetrievalRequest,
                onComplete: (Result<PlaylistRetrievalResponse>) -> Unit,
            ) {}
        }
    }

}