package com.ygorxkharo.obey.web.data.utils.images.impl.applemusic

import com.ygorxkharo.obey.web.data.playlists.integration.sdk.applemusic.dto.model.PlaylistArtWork
import kotlin.test.Test
import kotlin.test.assertEquals

class AppleMusicCoverImageBuilderTest {

    private val sut = object : AppleMusicCoverImageBuilder {
        override var imageWidth = 150
        override var imageHeight = 120
    }

    @Test
    fun test_When_BuildingCoverImageURLWithExistingArtworkURL_Expect_CoverImageUrlToBeReturned() {
        //Arrange
        val expectedCoverImageUrl = "https://artwork-150x120.png"
        val artwork = PlaylistArtWork(
            width = null,
            height = null,
            coverImageUrl = "https://artwork-{w}x{h}.png"
        )

        //Act
        val actualCoverImage = sut.buildCoverImageUrl(artwork)

        //Assert
        assertEquals(expectedCoverImageUrl, actualCoverImage)
    }

    @Test
    fun test_When_BuildingCoverImageURLWithNoExistingArtworkURL_Expect_DefaultCoverToBeReturned() {
        //Arrange
        val expectedCoverImageUrl = "images/backgrounds/bg_apple_music_playlist_cover_image_placeholder.jpg"
        val artwork = PlaylistArtWork(
            width = null,
            height = null,
            coverImageUrl = null
        )

        //Act
        val actualCoverImage = sut.buildCoverImageUrl(artwork)

        //Assert
        assertEquals(expectedCoverImageUrl, actualCoverImage)
    }

}