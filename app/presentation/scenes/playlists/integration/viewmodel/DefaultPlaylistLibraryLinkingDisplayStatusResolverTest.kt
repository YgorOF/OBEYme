package com.ygorxkharo.obey.web.app.presentation.scenes.playlists.integration.viewmodel

import com.ygorxkharo.obey.web.app.presentation.colors.AppColors
import com.ygorxkharo.obey.web.app.presentation.scenes.playlists.integration.model.PlaylistLibraryLinkingStatus
import com.ygorxkharo.obey.web.data.playlists.integration.MusicPlatformLibraryException
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultPlaylistLibraryLinkingDisplayStatusResolverTest {
    private lateinit var sut: DefaultPlaylistLibraryDisplayStatusResolver

    private val expectedStatusText = "Test Connected"
    private val expectedBackgroundColor = AppColors.NERO
    private val validStatusTextCollection = mapOf(PlaylistLibraryLinkingStatus.CONNECTED to expectedStatusText)
    private val invalidStatusTextCollection = HashMap<PlaylistLibraryLinkingStatus, String>()

    private val validBackgroundColorCollection = mapOf(PlaylistLibraryLinkingStatus.CONNECTED to expectedBackgroundColor)
    private val invalidBackgroundColorCollection = HashMap<PlaylistLibraryLinkingStatus, String>()
    private val libraryLinkingStatus = PlaylistLibraryLinkingStatus.CONNECTED

    @Test
    fun test_When_IntegrationHasNotYetStarted_Expect_StateToBeResolved() {
        //Arrange
        sut = DefaultPlaylistLibraryDisplayStatusResolver(validStatusTextCollection, validBackgroundColorCollection)
        val expectedLoadingStatus = PlaylistLibraryLinkingStatus.CONNECTED

        //Act
        val actualIntegrationState = sut.resolveDisplayStatus(libraryLinkingStatus)

        //Assert
        assertEquals(expectedStatusText, actualIntegrationState.statusText)
        assertEquals(expectedBackgroundColor, actualIntegrationState.backgroundHighlightColor)
        assertEquals(expectedLoadingStatus, actualIntegrationState.loadingStatus)
    }

    @Test
    fun test_When_StatusTextDoesNotExistForIntegrationStatus_Expect_ExceptionToBeThrown() {
        //Arrange
        sut = DefaultPlaylistLibraryDisplayStatusResolver(invalidStatusTextCollection, validBackgroundColorCollection)
        val expectedErrorMessage = "No status text value available for this integration status"

        //Act
        val actualErrorMessage = try {
            sut.resolveDisplayStatus(libraryLinkingStatus)
            ""
        } catch (ex: MusicPlatformLibraryException) {
            ex.message
        }

        //Assert
        assertEquals(expectedErrorMessage, actualErrorMessage)
    }

    @Test
    fun test_When_BackgroundColorDoesNotExistForIntegrationStatus_Expect_ExceptionToBeThrown() {
        //Arrange
        sut = DefaultPlaylistLibraryDisplayStatusResolver(validStatusTextCollection, invalidBackgroundColorCollection)
        val expectedErrorMessage = "No background color value available for this integration status"

        //Act
        val actualErrorMessage = try {
            sut.resolveDisplayStatus(libraryLinkingStatus)
            ""
        } catch (ex: MusicPlatformLibraryException) {
            ex.message
        }

        //Assert
        assertEquals(expectedErrorMessage, actualErrorMessage)
    }
}