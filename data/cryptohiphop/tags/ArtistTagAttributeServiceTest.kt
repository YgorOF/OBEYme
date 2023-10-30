package com.ygorxkharo.obey.web.data.cryptohiphop.tags

import com.ygorxkharo.obey.web.data.cryptohiphop.tags.repository.ArtistTagAttributeService
import com.ygorxkharo.obey.web.domain.cryptohiphop.tags.model.MusicTagRequest
import com.ygorxkharo.obey.web.domain.cryptohiphop.tags.model.TagAttributes
import kotlin.test.Test
import kotlin.test.assertTrue

class ArtistTagAttributeServiceTest {

    private val artistTagAttributesCollection = mapOf(
        "A\$AP Rocky" to TagAttributes(
            imageUrl = "",
            thumbnailImageUrl = "",
            widthInPx = 0,
            heightInPx = 0
        ),
    )

    private val sut = ArtistTagAttributeService(tagAttributesCollection = artistTagAttributesCollection)
    private var actualTagAttributes: TagAttributes? = null
    private val onGetTagAttributes: (TagAttributes?) -> Unit = { tagAttributes ->
        actualTagAttributes = tagAttributes
    }

    @Test
    fun test_When_TagExistsForArtist_Expect_OnGetTagAttributesToTriggerWithTagAttributes() {
        // Arrange
        val tagRequest = MusicTagRequest(searchTerm = "A\$AP Rocky")

        // Act
        sut.getTagAttributes(tagRequest, onGetTagAttributes)

        // Assert
        assertTrue(actualTagAttributes != null)
    }

    @Test
    fun test_When_NoTagExistsForArtist_Expect_OnGetTagAttributesToTriggerWithNothing() {
        // Arrange
        val tagRequest = MusicTagRequest(searchTerm = "Karma")

        // Act
        sut.getTagAttributes(tagRequest, onGetTagAttributes)

        // Assert
        assertTrue(actualTagAttributes == null)
    }
}