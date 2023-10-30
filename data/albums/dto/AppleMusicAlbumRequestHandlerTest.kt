package com.ygorxkharo.obey.web.data.albums.dto

import com.ygorxkharo.obey.web.data.common.mockHttpClient
import com.ygorxkharo.obey.web.domain.recordlabels.model.AlbumRequest
import com.ygorxkharo.obey.web.domain.recordlabels.model.AlbumResult
import org.w3c.fetch.RequestInit
import kotlin.test.Test
import kotlin.test.assertEquals

class AppleMusicAlbumRequestHandlerTest {

    private lateinit var sut: AppleMusicAlbumRequestHandler
    private lateinit var actualDestinationUrl: String
    private lateinit var actualRequestOptions: RequestInit
    private val onCaptureRequest: (String, RequestInit) -> Unit = { destinationUrl, requestOptions ->
        actualDestinationUrl = destinationUrl
        actualRequestOptions = requestOptions
    }
    private val appleMusicDevToken = "ey.sljfh1lkjhljhalfhalfjla;flasdhfladsh"
    private lateinit var actualAlbumResult: AlbumResult
    private val onRequestHandled: (AlbumResult) -> Unit = { result ->
        actualAlbumResult = result
    }

    private lateinit var actualAlbumRequestException: Exception
    private val onRequestFailed: (Exception) -> Unit = { exception ->
        actualAlbumRequestException = exception
    }

    private val appleMusicAccessToken = "AF.ewrl2rjwofaf8uasf0asfoalhjfajfjhad0f0aflkahdshf0as9fhaodf"
    private val albumRequest = AlbumRequest(
        albumCatalogId = "1274653476",
        accessToken = appleMusicAccessToken
    )
    private val albumResponsePayload = """
        {
            "data": [{
                "id": "1274653476",
                "type": "albums",
                "attributes": {
                    "recordLabel": "Bad Vibes Forever / EMPIRE",
                    "copyright": "℗ 2017 Bad Vibes Forever / EMPIRE"
                }
            }]
        }
    """.trimIndent()

    @Test
    fun test_When_RequestSentForAlbumData_Expect_DestinationURLandRequestMethodToBeSet() {
        //Arrange
        val expectedDestinationUrl = "https://api.music.apple.com/v1/catalog/za/albums/1274653476"
        val expectedRequestMethod = "GET"
        val httpClient = mockHttpClient(onCaptureRequest = onCaptureRequest)
        sut = AppleMusicAlbumRequestHandler(httpClient, developerToken = appleMusicDevToken)

        //Act
        sut.handleRetrievalRequest(
            albumRequest,
            onRequestHandled = onRequestHandled,
            onRequestFailed = onRequestFailed
        )

        //Assert
        assertEquals(expectedDestinationUrl, actualDestinationUrl)
        assertEquals(expectedRequestMethod, actualRequestOptions.method)
    }

    @Test
    fun test_When_RequestForAlbumSucceed_Expect_TheAlbumResultToBeReturned() {
        //Arrange
        val expectedAlbumCopyright = "℗ 2017 Bad Vibes Forever / EMPIRE"
        val httpClient = mockHttpClient(responseAsString = albumResponsePayload)
        sut = AppleMusicAlbumRequestHandler(httpClient, developerToken = appleMusicDevToken)

        //Act
        sut.handleRetrievalRequest(
            albumRequest,
            onRequestHandled = onRequestHandled,
            onRequestFailed = onRequestFailed
        )

        //Assert
        assertEquals(expectedAlbumCopyright, actualAlbumResult.copyrightDescription)
    }

    @Test
    fun test_When_RequestForAlbumFails_Expect_ExceptionToBeReturned() {
        //Arrange
        val expectedErrorMessage = "No album information available"
        val httpClient = mockHttpClient(errorMessage = expectedErrorMessage)
        sut = AppleMusicAlbumRequestHandler(httpClient, developerToken = appleMusicDevToken)

        //Act
        sut.handleRetrievalRequest(
            albumRequest,
            onRequestHandled = onRequestHandled,
            onRequestFailed = onRequestFailed
        )

        //Assert
        assertEquals(expectedErrorMessage, actualAlbumRequestException.cause?.message)
    }

}