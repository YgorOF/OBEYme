package com.ygorxkharo.obey.web.app

import kotlin.test.Test
import kotlin.test.assertEquals

class MainFileTest {

    @Test
    fun test_When_UserAgentIsIPhone_Expect_iOSVersionReturnedWhenVersionContains3Numbers() {
        //Arrange
        val expectedVersionNumber = "12.3.2"
        val userAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 12_3_2 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) FxiOS/1.0 Mobile/12F69 Safari/600.1.4"

        //Act
        val actualIOSVersion = getIOSVersion(userAgent)

        //Assert
        assertEquals(expectedVersionNumber, actualIOSVersion)
    }

    @Test
    fun test_When_UserAgentIsIPhone_Expect_iOSVersionReturnedWhenVersionContains2Numbers() {
        //Arrange
        val expectedVersionNumber = "9.2"
        val userAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 9_2 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) FxiOS/1.0 Mobile/12F69 Safari/600.1.4"

        //Act
        val actualIOSVersion = getIOSVersion(userAgent)

        //Assert
        assertEquals(expectedVersionNumber, actualIOSVersion)
    }

    @Test
    fun test_When_UserAgentIsNotIPhone_Expect_NullToBeReturned() {
        //Arrange
        val expectedVersionNumber = null
        val userAgent = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.88 Mobile Safari/537.36"

        //Act
        val actualIOSVersion = getIOSVersion(userAgent)

        //Assert
        assertEquals(expectedVersionNumber, actualIOSVersion)
    }
}