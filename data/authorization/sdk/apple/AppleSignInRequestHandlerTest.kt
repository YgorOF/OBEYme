package com.ygorxkharo.obey.web.data.authorization.sdk.apple

import kotlinx.browser.window
import org.w3c.dom.Window
import kotlin.test.AfterTest
import kotlin.test.assertEquals
import kotlin.test.Test
import com.ygorxkharo.obey.web.data.authorization.MusicPlatformAuthorizationException
import com.ygorxkharo.obey.web.data.common.browser.WebBrowserWindowProvider
import com.ygorxkharo.obey.web.data.common.browser.BrowserWindowEventListener

class AppleSignInRequestHandlerTest {

    private lateinit var sut: AppleSignInRequestHandler
    private val authorizationUrl = "http://example.com"

    // Browser window provider setup
    private val appWindow = window
    private var validMonitoredWindow: Window? = appWindow.open("https://w.com")

    // Monitoring window setup
    private val windowEventMonitor: BrowserWindowEventListener<String> = object: BrowserWindowEventListener<String> {
        override lateinit var onCloseWindow: () -> Unit
        override lateinit var onEventListeningStopped: (String) -> Unit

        override fun startEventListener(browserWindow: Window) {}
        override fun stopEventListener() {}
    }

    //On request handled setup
    private var actualResponsePayload: String? = null
    private val onRequestHandled: (String) -> Unit = {
        actualResponsePayload = it
    }

    //On request failed setup
    private var actualResponseError: Exception? = null
    private val onRequestFailure: (Exception) -> Unit = {
        actualResponseError = it
    }

    @AfterTest
    fun tearDown() {
        actualResponsePayload = null
    }

    @Test
    fun test_When_AuthorizationFlowIsTerminatedPrematurely_Expect_ExceptionIsThrown() {
        //Arrange
        sut = AppleSignInRequestHandler(
            authorizationUrl = authorizationUrl,
            browserWindowProvider = stubBrowserWindowProvider(currentWindow = validMonitoredWindow),
            windowEventMonitor = windowEventMonitor
        )
        val expectedErrorMessage = "Unable to get access token before window was closed"

        //Act
        sut.handleRequest(onRequestHandled, onRequestFailure)
        windowEventMonitor.onCloseWindow.invoke()

        //Assert
        assertEquals(expectedErrorMessage, actualResponseError?.message)
    }

    @Test
    fun test_When_AuthorizationFlowIsCompleted_Expect_HandledRequestToContainResponsePayload() {
        //Arrange
        sut = AppleSignInRequestHandler(
            authorizationUrl = authorizationUrl,
            browserWindowProvider = stubBrowserWindowProvider(currentWindow = validMonitoredWindow),
            windowEventMonitor = windowEventMonitor
        )
        val expectedResponsePayload = "https//example.com/#access_token"

        //Act
        sut.handleRequest(onRequestHandled, onRequestFailure)
        windowEventMonitor.onEventListeningStopped.invoke(expectedResponsePayload)

        //Assert
        assertEquals(expectedResponsePayload, actualResponsePayload)
    }

    @Test
    fun test_When_TheHandlerIsUnableToLaunchAWindow_Expect_AnExceptionToBeThrown() {
        //Arrange
        val expectedErrorMessage = "Unable to launch authentication window"
        sut = AppleSignInRequestHandler(
            authorizationUrl = authorizationUrl,
            browserWindowProvider = stubBrowserWindowProvider(currentWindow = null),
            windowEventMonitor = windowEventMonitor
        )

        //Act
        var actualErrorMessage = ""
        try {
            sut.handleRequest(onRequestHandled, onRequestFailure)
        } catch (ex: MusicPlatformAuthorizationException) {
            actualErrorMessage = ex.message as String
        }

        //Assert
        assertEquals(expectedErrorMessage, actualErrorMessage)
    }

    private fun stubBrowserWindowProvider(currentWindow: Window?): WebBrowserWindowProvider {
        return object: WebBrowserWindowProvider {
            override val parentWindow: Window
                get() = appWindow

            override fun provideNewWindow(destinationUrl: String, windowWidth: Int, windowHeight: Int): Window? {
                return currentWindow
            }
        }
    }
}