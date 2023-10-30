package com.ygorxkharo.obey.web.data.common.storage

import kotlinx.browser.localStorage
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StringCollectionStorageWrapperTest {

    private val sut = StringCollectionStorageWrapper()
    private val localStorageKey = "string_collection_store"

    private var actualStringCollectionResult: Result<List<String>>? = null
    private val onCompleteCallback: (Result<List<String>>) -> Unit = { stringCollectionResult ->
        actualStringCollectionResult = stringCollectionResult
    }

    @AfterTest
    fun tearDown() {
        actualStringCollectionResult = null
        localStorage.removeItem(localStorageKey)
    }

    @Test
    fun test_When_StorageDoesNotContainStringCollectionBasedOnKey_Expect_EmptyCollectionReturned() {
        // Arrange
        val expectedCollectionSize = 0

        // Act
        sut.getFromStorage(localStorageKey, onComplete = onCompleteCallback)

        // Assert
        assertTrue(actualStringCollectionResult?.isSuccess == true)
        assertEquals(expectedCollectionSize, actualStringCollectionResult?.getOrNull()?.size)
    }

    @Test
    fun test_When_StorageContainStringCollectionBasedOnKey_Expect_StringCollectionReturned() {
        // Arrange
        val expectedCollectionSize = 2
        val stringCollection = listOf("item 1", "item 2")
        localStorage.setItem(localStorageKey, JSON.stringify(stringCollection))

        // Act
        sut.getFromStorage(localStorageKey, onComplete = onCompleteCallback)

        // Assert
        assertTrue(actualStringCollectionResult?.isSuccess == true)
        assertEquals(expectedCollectionSize, actualStringCollectionResult?.getOrNull()?.size)
    }

    @Test
    fun test_When_SavingStringCollection_Expect_StringCollectionToBeSavedToStorage() {
        // Arrange
        val expectedCollectionSize = 8
        val stringCollection = listOf("item 1", "item 2", "item 3", "item 4", "item 5", "item 6", "item 7", "item 8")

        // Act
        sut.saveToStorage(localStorageKey, persistencePayload = stringCollection)

        // Assert
        val actualSavedCollectionStringValue = localStorage.getItem(localStorageKey) as String
        val actualSavedCollection = JSON.parse<Array<String>>(actualSavedCollectionStringValue)
        assertEquals(expectedCollectionSize, actualSavedCollection.size)
    }
}