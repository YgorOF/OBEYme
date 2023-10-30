package com.ygorxkharo.obey.web.data.playlists.linking.sdk.applemusic.dto

import com.ygorxkharo.obey.web.data.common.browser.http.HttpClient
import com.ygorxkharo.obey.web.data.platforms.MusicPlatform
import com.ygorxkharo.obey.web.data.playlists.integration.MusicPlatformLibraryException
import com.ygorxkharo.obey.web.data.playlists.integration.sdk.applemusic.dto.AppleMusicPlaylistRequestHandler
import com.ygorxkharo.obey.web.data.playlists.integration.sdk.applemusic.dto.model.PlaylistCollectionDto
import com.ygorxkharo.obey.web.data.playlists.linking.sdk.applemusic.AppleMusicPlaylistTestFixture
import com.ygorxkharo.obey.web.data.utils.JsonStringToObjectDeserializer
import com.ygorxkharo.obey.web.domain.common.Failure
import com.ygorxkharo.obey.web.domain.common.Result
import com.ygorxkharo.obey.web.domain.common.Success
import com.ygorxkharo.obey.web.domain.musicplatforms.playlists.linking.model.PlaylistRetrievalRequest
import org.w3c.fetch.RequestInit
import kotlin.test.Test
import kotlin.test.assertEquals

class AppleMusicPlaylistRequestHandlerTest {

    private lateinit var actualDestinationUrl: String
    private lateinit var actualPlaylistCollectionDto: PlaylistCollectionDto
    private val onRequestHandled: (PlaylistCollectionDto)  -> Unit = {
        actualPlaylistCollectionDto = it
    }

    private lateinit var actualPlaylistFailureException: Exception
    private val onRequestFailed: (Exception)  -> Unit = {
        actualPlaylistFailureException = it
    }

    private val musicPlatformAccessToken = "ey.lvcjalfjlkajflkj2lkjlajflkajdfa"
    private val playlistRetrievalRequest = PlaylistRetrievalRequest(
        musicPlatformAccessToken = musicPlatformAccessToken,
        musicPlatform = MusicPlatform.APPLE_MUSIC.value
    )
    private val developerToken = "ey.sfksljdfalk3ksjxjvhajhjdhfjsaf"

    @Test
    fun test_When_RequestIsSuccessful_Expect_onRequestHandledTriggeredWithSuccess() {
        //Arrange
        val playlistCollectionDtoString = AppleMusicPlaylistTestFixture.PLAYLIST_COLLECTION_DTO_PAYLOAD
        val expectedPlaylistCollectionDto = JsonStringToObjectDeserializer.convertFromString(
            PlaylistCollectionDto.serializer(),
            playlistCollectionDtoString
        )

        val playlistResponseErrorMessage = null
        val httpClient = stubHttpClient(playlistCollectionDtoString, playlistResponseErrorMessage)
        val sut = AppleMusicPlaylistRequestHandler(httpClient, developerToken)

        //Act
        sut.handlePlaylistRetrievalRequest(playlistRetrievalRequest, onRequestHandled, onRequestFailed)

        //Assert
        assertEquals(expectedPlaylistCollectionDto, actualPlaylistCollectionDto)
    }

    @Test
    fun test_When_RequestFails_Expect_onRequestFailedToTriggeredWithFailure() {
        //Arrange
        val playlistCollectionDtoString = null
        val expectedPlaylistResponseErrorMessage = "Unable to get playlists, due to authentication"
        val httpClient = stubHttpClient(playlistCollectionDtoString, expectedPlaylistResponseErrorMessage)
        val sut = AppleMusicPlaylistRequestHandler(httpClient, developerToken)

        //Act
        sut.handlePlaylistRetrievalRequest(playlistRetrievalRequest, onRequestHandled, onRequestFailed)

        //Assert
        assertEquals(expectedPlaylistResponseErrorMessage, actualPlaylistFailureException.message)
    }

    @Test
    fun test_When_RequestPayloadContainsNoLimit_Expect_TheOffsetToBeTheFirstQueryParameter() {
        //Arrange
        val playlistRetrievalRequest = PlaylistRetrievalRequest(
            musicPlatformAccessToken = musicPlatformAccessToken,
            musicPlatform = MusicPlatform.APPLE_MUSIC.value,
            playlistCountOffset = 25
        )
        val expectedDestinationUrl = "https://api.music.apple.com/v1/me/library/playlists?offset=25"
        val httpClient = stubHttpClient()
        val sut = AppleMusicPlaylistRequestHandler(httpClient, developerToken)

        //Act
        sut.handlePlaylistRetrievalRequest(playlistRetrievalRequest, onRequestHandled, onRequestFailed)

        //Assert
        assertEquals(expectedDestinationUrl, actualDestinationUrl)
    }

    @Test
    fun test_When_RequestPayloadContainsLimit_Expect_TheOffsetToBeTheFirstQueryParameter() {
        //Arrange
        val playlistRetrievalRequest = PlaylistRetrievalRequest(
            musicPlatformAccessToken = musicPlatformAccessToken,
            musicPlatform = MusicPlatform.APPLE_MUSIC.value,
            limit = 5,
            playlistCountOffset = 25
        )
        val expectedDestinationUrl = "https://api.music.apple.com/v1/me/library/playlists?limit=5&offset=25"
        val httpClient = stubHttpClient()
        val sut = AppleMusicPlaylistRequestHandler(httpClient, developerToken)

        //Act
        sut.handlePlaylistRetrievalRequest(playlistRetrievalRequest, onRequestHandled, onRequestFailed)

        //Assert
        assertEquals(expectedDestinationUrl, actualDestinationUrl)
    }

    private fun stubHttpClient(
        playlistResponsePayload: String? = null,
        playlistResponseErrorMessage: String? = null
    ): HttpClient<RequestInit, String> {
        return object: HttpClient<RequestInit, String> {
            override fun handleRequest(
                destinationUrl: String,
                requestOptions: RequestInit,
                onRequestHandled: (Result<String>) -> Unit
            ) {
                actualDestinationUrl = destinationUrl
                if(playlistResponsePayload != null) {
                    onRequestHandled(Success(playlistResponsePayload))
                }

                if(playlistResponseErrorMessage != null) {
                    val failureResult = Failure(MusicPlatformLibraryException(playlistResponseErrorMessage))
                    onRequestHandled(failureResult)
                }
            }
        }
    }
}