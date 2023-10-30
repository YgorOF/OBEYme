package com.ygorxkharo.obey.web.data.musiccharts

import com.ygorxkharo.obey.web.data.playlists.integration.MusicPlatformLibraryException
import com.ygorxkharo.obey.web.domain.musicplatforms.charts.MusicPlatformChartsService
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DefaultSongChartsServiceTest {

    private val musicPlatformName = "test_music_charts_service"
    private val invalidPlatformName = "invalid_music_charts_service"
    private val chartsServiceCollection = mapOf(musicPlatformName to mockMusicPlatformChartsService())
    private val accessToken = "ey.sfsfsfsf34tdgdffg4ydgdgdgd.Rasd3gdvb6585dd"
    private val sut = DefaultSongChartsService(chartsServiceCollection)
    private var isMusicPlatformChartsServiceCalled = false
    private val musicPlatformChartsServiceCallback: () -> Unit = {
        isMusicPlatformChartsServiceCalled = true
    }

    @Test
    fun test_When_MusicPlatformNameIsValid_Expect_MusicPlatformChartsServiceToBeCalled() {
        // Arrange
        // Act
        sut.getTopChartSongs(musicPlatformName, accessToken)

        // Assert
        assertTrue(isMusicPlatformChartsServiceCalled)
    }

    @Test
    fun test_When_MusicPlatformNameIsNotValid_Expect_ExceptionToBeThrown() {
        // Arrange
        val expectedErrorMessage = "No charts service found for music platform $invalidPlatformName"

        // Act
        val errorMessage = try {
            sut.getTopChartSongs(invalidPlatformName, accessToken)
            ""
        } catch (ex: MusicPlatformLibraryException) {
            ex.message
        }

        // Assert
        assertEquals(expectedErrorMessage, errorMessage)
    }

    private fun mockMusicPlatformChartsService(): MusicPlatformChartsService {
        return object: MusicPlatformChartsService {
            override fun getAllTopCharts(accessToken: String) {
                musicPlatformChartsServiceCallback.invoke()
            }
        }
    }
}