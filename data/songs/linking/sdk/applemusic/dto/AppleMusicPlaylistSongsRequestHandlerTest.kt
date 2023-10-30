package com.ygorxkharo.obey.web.data.songs.linking.sdk.applemusic.dto

import com.ygorxkharo.obey.web.data.common.browser.http.HttpClient
import com.ygorxkharo.obey.web.data.playlists.integration.MusicPlatformLibraryException
import com.ygorxkharo.obey.web.data.songs.linking.sdk.applemusic.AppleMusicLibrarySongTestFixture
import com.ygorxkharo.obey.web.data.songs.linking.sdk.applemusic.dto.model.LibrarySongCollectionDto
import com.ygorxkharo.obey.web.data.songs.linking.sdk.applemusic.dto.model.LibrarySongsRetrievalRequestDto
import com.ygorxkharo.obey.web.data.utils.JsonStringToObjectDeserializer
import com.ygorxkharo.obey.web.domain.common.Failure
import com.ygorxkharo.obey.web.domain.common.Result
import com.ygorxkharo.obey.web.domain.common.Success
import org.w3c.fetch.RequestInit
import kotlin.test.Test
import kotlin.test.assertEquals

class AppleMusicPlaylistSongsRequestHandlerTest {

    private lateinit var actualRequestOptions: RequestInit
    private lateinit var actualDestinationUrl: String
    private lateinit var actualSongsCollectionDTO: LibrarySongCollectionDto
    private val onRequestHandledCallback:(LibrarySongCollectionDto) -> Unit = {
        actualSongsCollectionDTO = it
    }
    private lateinit var actualRequestException: Exception
    private val onRequestFailedCallback: (Exception) -> Unit = {
        actualRequestException = it
    }
    private val developerToken = "ey.skahflj3n2nkfakdjnafjknadf"
    private val authorizationHeaderKey = "Authorization"
    private val accessTokenHeaderKey = "Music-User-Token"
    private val expectedAccessToken = "roanoked022424sfa.yefFFF"
    private val expectedResultLimit = 1
    private val expectedPlaylistId = "p.982sflhsdfljshf"
    private val songsRetrievalRequest = LibrarySongsRetrievalRequestDto(
        accessToken = expectedAccessToken,
        resultLimit = expectedResultLimit,
        playlistId = expectedPlaylistId
    )

    @Test
    fun test_When_PerformingSongsRetrievalRequest_Expect_DestinationURLToBeSet() {
        //Arrange
        val expectedDestinationUrl = "https://api.music.apple.com/v1/me/library/playlists/$expectedPlaylistId/tracks?limit=1"
        val httpClient = stubHttpClient()
        val sut = AppleMusicPlaylistSongsRequestHandler(httpClient, developerToken)

        //Act
        sut.handleSongsRetrievalRequest(songsRetrievalRequest, onRequestHandledCallback, onRequestFailedCallback)

        //Assert
        assertEquals(expectedDestinationUrl, actualDestinationUrl)
    }

    @Test
    fun test_When_PerformingSongsRetrievalRequestFoRecentlyPlayingSongs_Expect_DestinationURLToBeSet() {
        //Arrange
        val recentlyPlayedTracksRequest = LibrarySongsRetrievalRequestDto(
            accessToken = expectedAccessToken,
            resultLimit = expectedResultLimit,
            playlistId = "recently-played",
            countOffset = 20
        )
        val expectedDestinationUrl = "https://api.music.apple.com/v1/me/recent/played/tracks?offset=20"
        val httpClient = stubHttpClient()
        val sut = AppleMusicPlaylistSongsRequestHandler(httpClient, developerToken)

        //Act
        sut.handleSongsRetrievalRequest(recentlyPlayedTracksRequest, onRequestHandledCallback, onRequestFailedCallback)

        //Assert
        assertEquals(expectedDestinationUrl, actualDestinationUrl)
    }

    @Test
    fun test_When_PerformingSongsRetrievalRequest_Expect_RequestOptionsToBeSet() {
        //Arrange
        val expectedRequestMethod = "GET"
        val httpClient = stubHttpClient()
        val authTokenPrefix = "Bearer "
        val sut = AppleMusicPlaylistSongsRequestHandler(httpClient, developerToken)

        //Act
        sut.handleSongsRetrievalRequest(songsRetrievalRequest, onRequestHandledCallback, onRequestFailedCallback)

        //Assert
        assertEquals(expectedRequestMethod, actualRequestOptions.method)
        assertEquals(expectedAccessToken, actualRequestOptions.headers.get(accessTokenHeaderKey))
    }

    @Test
    fun test_When_RequestIsSuccessful_Expect_onRequestHandledTriggeredWithSuccess() {
        //Arrange
        val songCollectionDtoString = AppleMusicLibrarySongTestFixture.APPLE_MUSIC_LIBRARY_SONG_JSON_RESPONSE
        val expectedSongCollectionDto = JsonStringToObjectDeserializer.convertFromString(
            LibrarySongCollectionDto.serializer(),
            songCollectionDtoString
        )

        val songRetrievalResponseErrorMessage = null
        val httpClient = stubHttpClient(songCollectionDtoString, songRetrievalResponseErrorMessage)
        val sut = AppleMusicPlaylistSongsRequestHandler(httpClient, developerToken)

        //Act
        sut.handleSongsRetrievalRequest(songsRetrievalRequest, onRequestHandledCallback, onRequestFailedCallback)

        //Assert
        assertEquals(expectedSongCollectionDto, actualSongsCollectionDTO)
    }

    @Test
    fun test_When_RequestFails_Expect_onRequestFailedToTriggeredWithFailure() {
        //Arrange
        val songCollectionDtoString = null
        val expectedSongResponseErrorMessage = "Unable to get songs due to authentication error"
        val httpClient = stubHttpClient(songCollectionDtoString, expectedSongResponseErrorMessage)
        val sut = AppleMusicPlaylistSongsRequestHandler(httpClient, developerToken)

        //Act
        sut.handleSongsRetrievalRequest(songsRetrievalRequest, onRequestHandledCallback, onRequestFailedCallback)

        //Assert
        assertEquals(expectedSongResponseErrorMessage, actualRequestException.message)
    }

    private fun stubHttpClient(
        songsCollectionsResponsePayload: String? = null,
        errorMessage: String? = null
    ): HttpClient<RequestInit, String> {
        return object: HttpClient<RequestInit, String> {
            override fun handleRequest(
                destinationUrl: String,
                requestOptions: RequestInit,
                onRequestHandled: (Result<String>) -> Unit,
            ) {
                actualDestinationUrl = destinationUrl
                actualRequestOptions = requestOptions

                if(songsCollectionsResponsePayload != null) {
                    onRequestHandled(Success(songsCollectionsResponsePayload))
                }

                if(errorMessage != null) {
                    val failureResult = Failure(MusicPlatformLibraryException(errorMessage))
                    onRequestHandled(failureResult)
                }
            }
        }
    }

}