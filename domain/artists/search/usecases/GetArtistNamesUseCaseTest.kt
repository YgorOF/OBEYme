package com.ygorxkharo.obey.web.domain.artists.search.usecases

import com.ygorxkharo.obey.web.data.artists.search.ArtistSearchService
import com.ygorxkharo.obey.web.domain.artists.search.model.ArtistSearchException
import com.ygorxkharo.obey.web.domain.artists.search.model.ArtistSearchResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetArtistNamesUseCaseTest {

    private var actualSearchResult: Result<List<ArtistSearchResult>>? = null
    private val onCompleteCallback: (Result<List<ArtistSearchResult>>) -> Unit = { requestResult ->
        actualSearchResult = requestResult
    }
    private val expectedArtistName = "2Chain"
    private lateinit var actualSearchTerm: String
    private lateinit var actualOnCompleteCallback: (Result<List<ArtistSearchResult>>) -> Unit
    val onCallSearchService: ((String, (Result<List<ArtistSearchResult>>) -> Unit) -> Unit) = { searchTerm, onCompleteCallback ->
        actualSearchTerm = searchTerm
        actualOnCompleteCallback = onCompleteCallback
    }

    @Test
    fun test_When_InvokingUseCase_Expect_SearchTermAndOnCompleteCallbackToBeSetOnArtistSearchService() {
        // Arrange

        val artistSearchService = stubArtistSearchService(onCallSearchService = onCallSearchService)
        val sut = GetArtistNamesUseCase(artistSearchService)

        // Act
        sut.invoke(expectedArtistName, onCompleteCallback)

        // Assert
        assertEquals(expectedArtistName, actualSearchTerm)
        assertEquals(onCompleteCallback, actualOnCompleteCallback)
    }

    @Test
    fun test_When_ArtistSearchResultsRetrievedSuccessfully_Expect_SuccessResultToBeSentViaOnCompleteCallback() {
        //Arrange
        val expectedFirstArtistName = "2Chainz"
        val expectedSearchResult = listOf(
            ArtistSearchResult(artistName = expectedFirstArtistName),
            ArtistSearchResult(artistName = "2Chainz and Co")
        )
        val artistSearchService = stubArtistSearchService(searchResults = expectedSearchResult)
        val sut = GetArtistNamesUseCase(artistSearchService)

        //Act
        sut.invoke(expectedArtistName, onCompleteCallback)

        //Assert
        assertTrue(actualSearchResult?.isSuccess == true)
        assertEquals(expectedFirstArtistName, actualSearchResult?.getOrNull()?.first()?.artistName)
    }

    @Test
    fun test_When_ArtistSearchRequestFails_Expect_FailureResultToBeSentViaOnCompleteCallback() {
        //Arrange
        val expectedErrorMessage = "No results found for artist name"
        val artistSearchService = stubArtistSearchService(errorMessage = expectedErrorMessage)
        val sut = GetArtistNamesUseCase(artistSearchService)

        //Act
        sut.invoke(expectedArtistName, onCompleteCallback)

        //Assert
        assertTrue(actualSearchResult?.isFailure == true)
        assertEquals(expectedErrorMessage, actualSearchResult?.exceptionOrNull()?.message)
    }

    private fun stubArtistSearchService(
        onCallSearchService: ((String, (Result<List<ArtistSearchResult>>) -> Unit) -> Unit)? = null,
        searchResults: List<ArtistSearchResult>? = null,
        errorMessage: String? = null
    ): ArtistSearchService {
        return object: ArtistSearchService {
            override fun getArtistsByName(searchTerm: String, onComplete: (Result<List<ArtistSearchResult>>) -> Unit) {
                onCallSearchService?.invoke(searchTerm, onComplete)

                if(searchResults != null) {
                    return onComplete.invoke(Result.success(searchResults))
                }

                if(errorMessage != null) {
                    return onComplete.invoke(Result.failure(ArtistSearchException(errorMessage)))
                }
            }
        }
    }
}