package com.ygorxkharo.obey.web.data.cryptohiphop.tags

import com.ygorxkharo.obey.web.data.cryptohiphop.tags.repository.LocationTagService
import com.ygorxkharo.obey.web.domain.cryptohiphop.tags.model.MusicTagRequest
import com.ygorxkharo.obey.web.domain.cryptohiphop.tags.model.TagAttributes
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class LocationTagServiceTest {

    private val locationTagAttributes = mapOf(
        "location_1" to TagAttributes(
            imageUrl = "",
            thumbnailImageUrl = "",
            widthInPx = 0,
            heightInPx = 0
        )
    )

    private val sut = LocationTagService(locationTagAttributes)

    private var actualTagAttributes: TagAttributes? = null
    private val onGetTagAttributes: (TagAttributes?) -> Unit = { tagAttributes ->
        actualTagAttributes = tagAttributes
    }

    @BeforeTest
    fun setup() {
        actualTagAttributes = null
    }

    @Test
    fun test_When_ATagExistsForALocation_Expect_AttributeTagsToBeSentViaOnGetTagAttributesCallback() {
        // Arrange
        val tagRequest = MusicTagRequest(searchTerm = "location_1")

        // Act
        sut.getTagAttributes(tagRequest, onGetTagAttributes)

        // Assert
        assertTrue(actualTagAttributes != null)
    }

    @Test
    fun test_When_NoTagExistsForALocation_Expect_NothingToBeSentViaOnGetTagAttributesCallback() {
        // Arrange
        val tagRequest = MusicTagRequest(searchTerm = "location_2")

        // Act
        sut.getTagAttributes(tagRequest, onGetTagAttributes)

        // Assert
        assertTrue(actualTagAttributes == null)
    }
}