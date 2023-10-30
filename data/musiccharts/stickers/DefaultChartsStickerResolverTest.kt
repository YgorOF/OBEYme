package com.ygorxkharo.obey.web.data.musiccharts.stickers

import com.ygorxkharo.obey.web.domain.musicplatforms.charts.model.TopChart
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultChartsStickerResolverTest {

    private val firstChartName = "test chart 1"
    private val expectedImageUrl1 = "test_chart_1_img_url"
    private val TEST_TOP_CHART_1 = object: TopChart {
        override var platformId = "id.skdjfskfs"
        override var ordinalRank = 1
        override var chartName = firstChartName
        override var stickerImageUrl = expectedImageUrl1
    }

    private val secondChartName = "test chart 2"
    private val expectedImageUrl2 = "test_chart_2_img_url"
    private val TEST_TOP_CHART_2 = object: TopChart {
        override var platformId = "id.djfd00dfjskfs"
        override var ordinalRank = 2
        override var chartName = secondChartName
        override var stickerImageUrl = expectedImageUrl2
    }
    private val artistAndSongName = "test artist-test song name"
    private val sut = DefaultChartsStickerResolver()

    @Test
    fun test_When_songArtistNameAppearsOnlyInFirstChart_Expect_FirstChartImageUrlToBeReturned() {
        // Arrange
        val topChartCollection = mapOf(
            TEST_TOP_CHART_1 to arrayOf(artistAndSongName),
            TEST_TOP_CHART_2 to emptyArray()
        )

        // Act
        val actualImageUrl = sut.resolveChartStickerImageUrl(topChartCollection, artistAndSongName)

        // Assert
        assertEquals(expectedImageUrl1, actualImageUrl)
    }

    @Test
    fun test_When_songArtistNameAppearsInBothCharts_Expect_SecondChartImageUrlToBeReturned() {
        // Arrange
        val topChartCollection = mapOf(
            TEST_TOP_CHART_1 to arrayOf(artistAndSongName),
            TEST_TOP_CHART_2 to arrayOf(artistAndSongName)
        )

        // Act
        val actualImageUrl = sut.resolveChartStickerImageUrl(topChartCollection, artistAndSongName)

        // Assert
        assertEquals(expectedImageUrl2, actualImageUrl)
    }

    @Test
    fun test_When_songArtistNameAppearsInNoneOfTheCharts_Expect_AnEmptyValueToBeReturned() {
        // Arrange
        val expectedImageUrl = ""
        val topChartCollection = mapOf<TopChart, Array<String>>(
            TEST_TOP_CHART_1 to emptyArray(),
            TEST_TOP_CHART_2 to emptyArray()
        )

        // Act
        val actualImageUrl = sut.resolveChartStickerImageUrl(topChartCollection, artistAndSongName)

        // Assert
        assertEquals(expectedImageUrl, actualImageUrl)
    }
}