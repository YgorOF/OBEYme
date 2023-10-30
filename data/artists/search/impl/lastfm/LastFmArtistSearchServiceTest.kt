package com.ygorxkharo.obey.web.data.artists.search.impl.lastfm

import com.ygorxkharo.obey.web.data.artists.search.ArtistSearchService
import com.ygorxkharo.obey.web.data.common.browser.http.HttpClient
import com.ygorxkharo.obey.web.domain.artists.search.model.ArtistSearchException
import com.ygorxkharo.obey.web.domain.artists.search.model.ArtistSearchResult
import com.ygorxkharo.obey.web.domain.common.Failure
import com.ygorxkharo.obey.web.domain.common.Result
import com.ygorxkharo.obey.web.domain.common.Success
import org.w3c.fetch.RequestInit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LastFmArtistSearchServiceTest {

    private lateinit var sut: ArtistSearchService
    private lateinit var actualDestinationUrl: String
    private lateinit var actualRequestOptions: RequestInit
    private val onCaptureRequest: (String, RequestInit) -> Unit = { destinationUrl, requestOptions ->
        actualDestinationUrl = destinationUrl
        actualRequestOptions = requestOptions
    }
    private val lastFMApiKey = "34340uskflaffjsa0uqurosjfaljfadf"
    private var actualSearchResult: kotlin.Result<List<ArtistSearchResult>>? = null
    private val onCompleteRequest: (kotlin.Result<List<ArtistSearchResult>>) -> Unit = { searchResult ->
        actualSearchResult = searchResult
    }
    private val artistSearchTermValue = "Ed Shee"

    @Test
    fun test_When_SendingRequestToSearchArtistNames_Expect_DestinationURLAndRequestOptionsToBeSet() {
        // Arrange
        val expectedDestinationUrl = "https://ws.audioscrobbler.com/2.0?format=json&method=artist.search&limit=5&api_key=$lastFMApiKey&artist=$artistSearchTermValue"
        val expectedRequestMethod = "GET"
        val httpClient = mockHttpClient(onCaptureRequest = onCaptureRequest)
        sut = LastFmArtistSearchService(httpClient, lastFMApiKey)

        // Act
        sut.getArtistsByName(artistSearchTermValue, onCompleteRequest)

        // Assert
        assertEquals(expectedDestinationUrl, actualDestinationUrl)
        assertEquals(expectedRequestMethod, actualRequestOptions.method)
    }

    @Test
    fun test_When_SearchResultsAreAcquiredSuccessfully_Expect_CollectionOfArtistSearchResultToBeReturned() {
        // Arrange
        val searchResult = LastFmSearchResultTestFixture.ARTIST_SEARCH_RESULT_RESPONSE
        val httpClient = mockHttpClient(searchResultAsString = searchResult)
        sut = LastFmArtistSearchService(httpClient, lastFMApiKey)
        val expectedFirstResultValue = "Ed Sheeran"

        // Act
        sut.getArtistsByName(artistSearchTermValue, onCompleteRequest)

        // Assert
        assertTrue(actualSearchResult?.isSuccess == true)
        val firstSearchResult = actualSearchResult?.getOrNull()?.first() as ArtistSearchResult
        assertEquals(expectedFirstResultValue, firstSearchResult.artistName)
    }

    @Test
    fun test_When_ErrorOccursDuringArtistSearch_Expect_AFailureResultContainingAnException() {
        // Arrange
        val expectedErrorMessage = "No results found for this artist name"
        val httpClient = mockHttpClient(errorMessage = expectedErrorMessage)
        sut = LastFmArtistSearchService(httpClient, lastFMApiKey)

        // Act
        sut.getArtistsByName(artistSearchTermValue, onCompleteRequest)

        // Assert
        assertTrue(actualSearchResult?.isFailure == true)
        assertTrue(actualSearchResult?.exceptionOrNull() is ArtistSearchException)
        assertEquals(expectedErrorMessage, actualSearchResult?.exceptionOrNull()?.message)
    }

    private fun mockHttpClient(
        onCaptureRequest: ((String, RequestInit) -> Unit)? = null,
        searchResultAsString: String? = null,
        errorMessage: String? = null
    ): HttpClient<RequestInit, String> {
        return object: HttpClient<RequestInit, String> {
            override fun handleRequest(
                destinationUrl: String,
                requestOptions: RequestInit,
                onRequestHandled: (Result<String>) -> Unit,
            ) {
                onCaptureRequest?.invoke(destinationUrl, requestOptions)

                if(searchResultAsString != null) {
                    return onRequestHandled.invoke(Success(searchResultAsString))
                }

                if(errorMessage != null) {
                    val errorException = ArtistSearchException(errorMessage)
                    return onRequestHandled.invoke(Failure(errorException))
                }
            }
        }
    }

}