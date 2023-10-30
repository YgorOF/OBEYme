package com.ygorxkharo.obey.web.data.cryptohiphop

import com.ygorxkharo.obey.web.data.common.CollectionShuffler
import com.ygorxkharo.obey.web.data.cryptohiphop.tags.repository.GeneralTagAttributesService
import com.ygorxkharo.obey.web.domain.cryptohiphop.tags.model.MusicTagRequest
import com.ygorxkharo.obey.web.domain.cryptohiphop.tags.model.TagAttributes
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GeneralTagAttributesServiceTest {

    private val collectionShuffler = stubCollectionShuffler()
    private var actualTagAttributes: TagAttributes? = null
    private val onGetTagAttributes: (TagAttributes?) -> Unit = { tagAttributes ->
        actualTagAttributes = tagAttributes
    }
    private val tagRequest = MusicTagRequest(searchTerm = "")

    @BeforeTest
    fun setup() {
        actualTagAttributes = null
    }

    @Test
    fun test_When_GettingGeneralTagWhenCollectionIsEmpty_ExpectErrorToBeThrown() {
        // Arrange
        val expectedErrorMessage = "General tag attributes collection cannot be empty"
        val generalTagAttributes = mapOf<String, TagAttributes>()

        val sut = GeneralTagAttributesService(
            tagAttributesCollection = generalTagAttributes,
            collectionShuffler = collectionShuffler
        )

        // Act
        val actualErrorMessage = try {
            sut.getTagAttributes(tagRequest, onGetTagAttributes)
            ""
        } catch (ex: IllegalArgumentException) {
            ex.message
        }

        // Assert
        assertEquals(expectedErrorMessage, actualErrorMessage)
    }

    @Test
    fun test_When_GettingGeneralTagWhenCollectionIsNotEmpty_ExpectATagToBeReturned() {
        // Arrange
        val expectedTagUrl = "http://tag2.png"
        val generalTagAttributes = mapOf(
            "general_tag_1" to TagAttributes(
                imageUrl = "",
                thumbnailImageUrl = "",
                widthInPx = 0,
                heightInPx = 0
            ),
            "general_tag_2" to TagAttributes(
                imageUrl = "http://tag2.png",
                thumbnailImageUrl = "http://tag2_mini.png",
                widthInPx = 0,
                heightInPx = 0
            )
        )

        val sut = GeneralTagAttributesService(
            tagAttributesCollection = generalTagAttributes,
            collectionShuffler = collectionShuffler
        )

        // Act
        sut.getTagAttributes(tagRequest, onGetTagAttributes)

        // Assert
        assertEquals(expectedTagUrl, actualTagAttributes?.imageUrl)
    }

    private fun stubCollectionShuffler(): CollectionShuffler {
        return object: CollectionShuffler {
            override fun <T> shuffle(collection: Collection<T>): Collection<T> {
                val secondItem = collection.toMutableList()[1]
                val firstItem = collection.toMutableList()[0]
                return listOf(secondItem, firstItem)
            }
        }
    }
}