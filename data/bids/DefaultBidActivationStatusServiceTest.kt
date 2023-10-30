package com.ygorxkharo.obey.web.data.bids

import com.ygorxkharo.obey.web.data.futures.storage.WritableStorageWrapper
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultBidActivationStatusServiceTest {

    private var actualSaveSongNameResult: Boolean = false
    private var onSaveSongNameCallback: (Boolean) -> Unit = { result ->
        actualSaveSongNameResult = result
    }

    private var actualCheckBidStatusResult: Boolean = false
    private var onCheckBidStatusCallback: (Boolean) -> Unit = { result ->
        actualCheckBidStatusResult = result
    }

    private val songName = "test song name-song artist name"

    @Test
    fun test_When_SongNameToSaveIsEmpty_Expect_ExceptionIsThrown() {
        // Arrange
        val expectedErrorMessage = "The song name to save to the bid activation cannot be empty"
        val songName = ""
        val storageWrapper = stubStorageWrapper()
        val sut = DefaultBidActivationStatusService(storageWrapper)

        val actualErrorMessage = try {
            // Act
            sut.saveSongName(songName = songName, onSaveSongNameCallback)
            ""
        } catch (ex: Exception) {
            ex.message
        }

        // Assert
        assertEquals(expectedErrorMessage, actualErrorMessage)
    }

    @Test
    fun test_When_SongNameIsSavedSuccessfully_Expect_SaveCallbackToTriggerWithTrue() {
        // Arrange
        val expectedSaveSongNameResult = true
        val songCollection = ArrayList<String>()
        val storageWrapper = stubStorageWrapper(songNameCollection = songCollection)
        val sut = DefaultBidActivationStatusService(storageWrapper)

        // Act
        sut.saveSongName(songName = songName, onSaveSongNameCallback)

        // Assert
        assertEquals(expectedSaveSongNameResult, actualSaveSongNameResult)
    }

    @Test
    fun test_When_ErrorOccursWhenSavingSongName_Expect_SaveCallbackToTriggerWithFalse() {
        // Arrange
        val expectedSaveSongNameResult = false
        val errorMessage = "Invalid collection key provided for song collection"
        val storageWrapper = stubStorageWrapper(errorMessage = errorMessage)
        val sut = DefaultBidActivationStatusService(storageWrapper)

        // Act
        sut.saveSongName(songName = songName, onSaveSongNameCallback)

        // Assert
        assertEquals(expectedSaveSongNameResult, actualSaveSongNameResult)
    }

    @Test
    fun test_When_SongNameExistsInStorageForBids_Expect_BidStatusCheckToTriggerWithTrue() {
        // Arrange
        val expectedBidStatus = true
        val songCollection = listOf(songName)
        val storageWrapper = stubStorageWrapper(songNameCollection = songCollection)
        val sut = DefaultBidActivationStatusService(storageWrapper)

        // Act
        sut.checkSongBidStatus(songName = songName, onCheckBidStatusCallback)

        // Assert
        assertEquals(expectedBidStatus, actualCheckBidStatusResult)
    }

    @Test
    fun test_When_SongNameIsNotInStorageForBids_Expect_BidStatusCheckToTriggerWithFalse() {
        // Arrange
        val expectedBidStatus = false
        val songCollection = ArrayList<String>()
        val storageWrapper = stubStorageWrapper(songNameCollection = songCollection)
        val sut = DefaultBidActivationStatusService(storageWrapper)

        // Act
        sut.checkSongBidStatus(songName = songName, onCheckBidStatusCallback)

        // Assert
        assertEquals(expectedBidStatus, actualCheckBidStatusResult)
    }

    @Test
    fun test_When_CheckForBidStatusFailsDueToError_Expect_BidStatusCheckToTriggerWithFalse() {
        // Arrange
        val expectedBidStatus = false
        val errorMessage = "Unable to get song name collection to check bid status"
        val storageWrapper = stubStorageWrapper(errorMessage = errorMessage)
        val sut = DefaultBidActivationStatusService(storageWrapper)

        // Act
        sut.checkSongBidStatus(songName = songName, onCheckBidStatusCallback)

        // Assert
        assertEquals(expectedBidStatus, actualCheckBidStatusResult)
    }

    private fun stubStorageWrapper(
        songNameCollection: List<String>? = null,
        errorMessage: String? = null
    ): WritableStorageWrapper<List<String>> {

        return object: WritableStorageWrapper<List<String>> {
            override fun saveToStorage(objectKey: String, persistencePayload: List<String>) {}

            override fun getFromStorage(objectKey: String, onComplete: (Result<List<String>>) -> Unit) {
                if(songNameCollection != null) {
                    onComplete.invoke(Result.success(songNameCollection))
                }

                if(errorMessage != null) {
                    onComplete.invoke(Result.failure(IllegalStateException(errorMessage)))
                }
            }
        }
    }


}