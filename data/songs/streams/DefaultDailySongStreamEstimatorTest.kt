package com.ygorxkharo.obey.web.data.songs.streams

import kotlin.js.Date
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultDailySongStreamEstimatorTest {

    @Test
    fun test_When_EstimatingDailySongsStreams_Expect_StreamCountToBeProportionalToHoursPassedSinceMidnight() {
        //Arrange
        val sut = DefaultDailySongStreamEstimator(dailySongStreamCap = 10)
        val dateToday = Date("2024-12-04T19:00:00")
        println("Date today: ${dateToday.toLocaleDateString()}")
        val expectedStreamsToday = 8

        //Act
        val actualStreamsToday = sut.getTotalStreamsEstimate(dateToday)

        //Assert
        assertEquals(expectedStreamsToday, actualStreamsToday)
    }

}