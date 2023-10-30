package com.ygorxkharo.obey.web.data.creditfacility.offer

import com.ygorxkharo.obey.web.data.creditfacility.common.StreamingEngagement
import kotlin.test.Test
import kotlin.test.assertEquals

class StreamingEngagementTest {

    @Test
    fun test_When_TheUserStreamCountIsAbove1500_Expect_TheUserToBeASeriousUser() {
        //Arrange
        val expectedStreamingEngagement = StreamingEngagement.SERIOUS_USER
        val streamCount = 1800

        //Act
        val actualStreamingEngagement = StreamingEngagement.fromEngagementRange(streamCount)

        //Assert
        assertEquals(expectedStreamingEngagement, actualStreamingEngagement)
    }

    @Test
    fun test_When_TheUserStreamCountIsBetween600And1500_Expect_TheUserToBeADopeUser() {
        //Arrange
        val expectedStreamingEngagement = StreamingEngagement.DOPE_USER
        val streamCount = 732

        //Act
        val actualStreamingEngagement = StreamingEngagement.fromEngagementRange(streamCount)

        //Assert
        assertEquals(expectedStreamingEngagement, actualStreamingEngagement)
    }

    @Test
    fun test_When_TheUserStreamCountBelow600_Expect_TheUserToBeAnAvidUser() {
        //Arrange
        val expectedStreamingEngagement = StreamingEngagement.AVID_USER
        val streamCount = 148

        //Act
        val actualStreamingEngagement = StreamingEngagement.fromEngagementRange(streamCount)

        //Assert
        assertEquals(expectedStreamingEngagement, actualStreamingEngagement)
    }

    @Test
    fun test_When_ANegativeValueIsProvided_Expect_AnErrorToBeThrown() {
        //Arrange
        val expectedErrorMessage = "Invalid engagement level value provided. Make sure the value is above zero"
        val streamCount = -100

        //Act
        val actualErrorMessage = try {
            StreamingEngagement.fromEngagementRange(streamCount)
            ""
        } catch (ex: Exception) {
            ex.message
        }

        //Assert
        assertEquals(expectedErrorMessage, actualErrorMessage)
    }
}