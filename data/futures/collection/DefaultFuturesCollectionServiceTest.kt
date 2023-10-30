package com.ygorxkharo.obey.web.data.futures.collection

import com.ygorxkharo.obey.web.data.futures.storage.StorageWrapper
import com.ygorxkharo.obey.web.data.playlists.integration.MusicPlatformLibraryException
import com.ygorxkharo.obey.web.domain.futures.model.FuturesCollectionRequest
import com.ygorxkharo.obey.web.domain.futures.model.FuturesTerm
import com.ygorxkharo.obey.web.domain.musicroom.model.SongFuturesAttributes
import com.ygorxkharo.obey.web.domain.musicroom.model.SongFuturesOwner
import com.ygorxkharo.obey.web.domain.musicroom.model.SongFuturesStreamMetrics
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DefaultFuturesCollectionServiceTest {

    private lateinit var sut: DefaultFuturesCollectionService
    private val futuresCollectionRequest = FuturesCollectionRequest()
    private lateinit var storageWrapper: StorageWrapper<List<SongFuturesAttributes>>
    private var actualSongFuturesCollectionResult: Result<List<SongFuturesAttributes>>? = null
    private val songFuturesCollectionCallback: (Result<List<SongFuturesAttributes>>) -> Unit = {
        actualSongFuturesCollectionResult = it
    }

    @Test
    fun test_When_GettingUserFuturesCollectionSuccessfully_Expect_FuturesToBeReturnedSpreadAcrossDifferentTerms() {
        // Arrange
        storageWrapper = stubStorageWrapper(hasSongFuturesCollection = true)
        sut = DefaultFuturesCollectionService(storageWrapper)

        // Act
        sut.getCurrentFuturesCollection(futuresCollectionRequest, songFuturesCollectionCallback)

        // Assert
        assertTrue(actualSongFuturesCollectionResult?.isSuccess == true)
        val songFuturesCollection = actualSongFuturesCollectionResult?.getOrDefault(emptyList())
        for(index in 0 until 4) {
            val currentSongFutureItem = songFuturesCollection?.get(index)
            assertTrue(currentSongFutureItem?.isDelivered == true)
        }

        for(index in 0 until 2) {
            val currentSongFutureItem = songFuturesCollection?.get(index)
            assertTrue(currentSongFutureItem?.isProfitable == true)
        }
    }

    @Test
    fun test_When_GettingUserFuturesCollectionSuccessfully_Expect_ThereExistsAFuturesItemUnderEachSubCollection() {
        // Arrange
        storageWrapper = stubStorageWrapper(hasSongFuturesCollection = true)
        sut = DefaultFuturesCollectionService(storageWrapper)

        // Act
        sut.getCurrentFuturesCollection(futuresCollectionRequest, songFuturesCollectionCallback)

        // Assert
        assertTrue(actualSongFuturesCollectionResult?.isSuccess == true)
        val songFuturesCollection = actualSongFuturesCollectionResult?.getOrDefault(emptyList())?.map { futuresItem -> futuresItem.futuresTerm }
        assertTrue(songFuturesCollection?.contains(FuturesTerm.ONE_DAY) == true)
        assertTrue(songFuturesCollection?.contains(FuturesTerm.ONE_WEEK) == true)
        assertTrue(songFuturesCollection?.contains(FuturesTerm.ONE_MONTH) == true)
        assertTrue(songFuturesCollection?.contains(FuturesTerm.ONE_YEAR) == true)
        assertTrue(songFuturesCollection?.contains(FuturesTerm.FIVE_YEAR) == true)
    }

    @Test
    fun test_When_UserFutureCollectionRequestFails_Expect_AnFailureResultIsReturned() {
        // Arrange
        val expectedErrorMessage = "Unable to get futures collection for your account"
        storageWrapper = stubStorageWrapper(hasErrorMessage = true)
        sut = DefaultFuturesCollectionService(storageWrapper)

        // Act
        sut.getCurrentFuturesCollection(futuresCollectionRequest, songFuturesCollectionCallback)

        // Assert
        assertTrue(actualSongFuturesCollectionResult?.isFailure == true)
        val songFuturesRequestException = actualSongFuturesCollectionResult?.exceptionOrNull()
        assertEquals(expectedErrorMessage, songFuturesRequestException?.message)
    }

    private fun stubStorageWrapper(
        hasSongFuturesCollection: Boolean = false,
        hasErrorMessage: Boolean = false
    ): StorageWrapper<List<SongFuturesAttributes>> {
        return object: StorageWrapper<List<SongFuturesAttributes>> {
            override fun getFromStorage(objectKey: String, onComplete: (Result<List<SongFuturesAttributes>>) -> Unit) {
                if(hasSongFuturesCollection) {
                    val futuresCollection = ArrayList<SongFuturesAttributes>()
                    for(currentFuturesCount in 0 until 8) {
                        val songFuturesAttributes = SongFuturesAttributes(
                            songFuturesOwner = SongFuturesOwner(),
                            streamingMetrics = SongFuturesStreamMetrics()
                        )
                        futuresCollection.add(songFuturesAttributes)
                    }
                    onComplete.invoke(Result.success(futuresCollection))
                }

                if(hasErrorMessage) {
                    val exception = MusicPlatformLibraryException("Unable to get futures collection for your account")
                    onComplete.invoke(Result.failure(exception))
                }
            }
        }
    }
}