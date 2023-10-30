package com.ygorxkharo.obey.web.domain.radiostations.usecases

import com.ygorxkharo.obey.web.data.playlists.integration.MusicPlatformLibraryException
import com.ygorxkharo.obey.web.domain.common.Failure
import com.ygorxkharo.obey.web.domain.common.Result
import com.ygorxkharo.obey.web.domain.common.Success
import com.ygorxkharo.obey.web.domain.musicplatforms.playlists.linking.MusicPlatformPlaylistService
import com.ygorxkharo.obey.web.domain.musicplatforms.playlists.linking.model.PlaylistRetrievalRequest
import com.ygorxkharo.obey.web.domain.musicplatforms.playlists.linking.model.PlaylistRetrievalResponse
import com.ygorxkharo.obey.web.domain.musicplatforms.radiostations.model.MusicPlatformRadioStation
import com.ygorxkharo.obey.web.domain.musicplatforms.radiostations.model.RadioStationRetrievalRequest
import com.ygorxkharo.obey.web.domain.musicplatforms.radiostations.model.RadioStationRetrievalResponse
import com.ygorxkharo.obey.web.domain.musicplatforms.radiostations.usecases.GetPersonalUserRadioStationUseCase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetPersonalUserRadioStationUseCaseTest {

    private val musicPlatform = "test"
    private val musicPlatformAccessToken = "ey.xvxlkjalj0s9fwijflajflajdfasfasf"
    private lateinit var actualRadioStationRetrievalRequest: RadioStationRetrievalRequest
    private lateinit var actualOnCompleteCallback: (Result<RadioStationRetrievalResponse>) -> Unit
    private val countryCode = "za"
    private val expectedRadioStationRetrievalRequest = RadioStationRetrievalRequest(
        musicPlatform = musicPlatform,
        musicPlatformAccessToken = musicPlatformAccessToken,
        countryCode = countryCode
    )

    private val expectedRadioStationRetrievalResponse = RadioStationRetrievalResponse(
        radioStation = MusicPlatformRadioStation(
            id = "ey.slshfahfa",
            coverArtImageUrl = "https://example-cover-image.png",
            title = "User's Radio Station"
        )
    )

    private lateinit var actualResult: Result<RadioStationRetrievalResponse>
    private val onCompleteCallback: (Result<RadioStationRetrievalResponse>) -> Unit = {
        actualResult = it
    }

    @Test
    fun test_When_InvokingUseCaseWithSuccess_Expect_SuccessResultToBeReceivedOnComplete() {
        //Arrange
        val musicPlatformPlaylistService = stubLinkedPlaylistsService(radioStationRetrievalResponse = expectedRadioStationRetrievalResponse)
        val sut = GetPersonalUserRadioStationUseCase(musicPlatformPlaylistService)

        //Act
        sut.invoke(expectedRadioStationRetrievalRequest, onCompleteCallback)

        //Assert
        assertTrue(actualResult is Success)
        assertEquals(expectedRadioStationRetrievalResponse, (actualResult as Success).payload)
    }

    @Test
    fun test_When_InvokingUseCaseWithFailure_Expect_FailureResultToBeReceivedOnComplete() {
        //Arrange
        val expectedErrorMessage = "Error getting radio station"
        val musicPlatformPlaylistService = stubLinkedPlaylistsService(errorMessage = expectedErrorMessage)
        val sut = GetPersonalUserRadioStationUseCase(musicPlatformPlaylistService)

        //Act
        sut.invoke(expectedRadioStationRetrievalRequest, onCompleteCallback)

        //Assert
        assertTrue(actualResult is Failure)
        assertEquals(expectedErrorMessage, (actualResult as Failure).error.message)
    }

    private fun stubLinkedPlaylistsService(
        radioStationRetrievalResponse: RadioStationRetrievalResponse? = null,
        errorMessage: String? = null
    ): MusicPlatformPlaylistService {
        return object: MusicPlatformPlaylistService {
            override fun getPlaylistsAsync(
                playlistRetrievalRequest: PlaylistRetrievalRequest,
                onComplete: (Result<PlaylistRetrievalResponse>) -> Unit
            ) {}

            override fun getUserPersonalRadioStationAsync(
                radioStationRetrievalRequest: RadioStationRetrievalRequest,
                onComplete: (Result<RadioStationRetrievalResponse>) -> Unit,
            ) {
                actualRadioStationRetrievalRequest = radioStationRetrievalRequest
                actualOnCompleteCallback = onComplete

                if(radioStationRetrievalResponse != null) {
                    return onComplete.invoke(Success(radioStationRetrievalResponse))
                }

                if(errorMessage != null) {
                    val libraryError = MusicPlatformLibraryException(errorMessage)
                    onComplete(Failure(libraryError))
                }
            }

            override fun getUserRecommendations(
                recommendationsRequest: PlaylistRetrievalRequest,
                onComplete: (Result<PlaylistRetrievalResponse>) -> Unit,
            ) {}
        }
    }

}