package com.ygorxkharo.obey.web.data.common.browser

import kotlinx.browser.window
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DefaultWebBrowserWindowProviderTest {

    @Test
    fun test_When_BrowserWindowIsCreated_Expect_TheWindowPropertiesToBeSet() {
        //Arrange
        val expectedUrl = "about:blank"
        val expectedWindowWidth = 320
        val expectedWindowHeight = 350
        val appWindow = window
        val sut = DefaultWebBrowserWindowProvider(appWindow)

        //Act
        val actualWindow = sut.provideNewWindow(expectedUrl, expectedWindowWidth, expectedWindowHeight)

        //Assert
        assertEquals(expectedWindowWidth, actualWindow!!.outerWidth)
        assertEquals(expectedWindowHeight, actualWindow.outerHeight)
        assertEquals(expectedUrl, actualWindow.location.href)
    }
}