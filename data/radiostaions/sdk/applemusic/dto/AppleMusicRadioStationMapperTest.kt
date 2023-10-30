package com.ygorxkharo.obey.web.data.radiostaions.sdk.applemusic.dto

import com.ygorxkharo.obey.web.data.radiostations.sdk.applemusic.dto.AppleMusicRadioStationMapper
import com.ygorxkharo.obey.web.data.radiostations.sdk.applemusic.dto.model.RadioStationDto
import com.ygorxkharo.obey.web.data.utils.JsonStringToObjectDeserializer
import kotlin.test.Test
import kotlin.test.assertEquals

class AppleMusicRadioStationMapperTest {

    private val radioStationDtoJson = AppleMusicRadioStationTestFixture.RADIO_STATION_DTO_PAYLOAD
    private val radioStationDto = JsonStringToObjectDeserializer.convertFromString(
        RadioStationDto.serializer(),
        radioStationDtoJson
    )

    @Test
    fun test_When_ConvertingAnAppleMusicRadioStationDTO_Expect_AMusicPlatformRadioStationToBeReturned() {
        //Arrange
        val sut = AppleMusicRadioStationMapper(
            imageWidth = 2400,
            imageHeight = 2400
        )
        val expectedId = "ra.u-c431007bd49dd9891abb70ff757aa95f"
        val expectedTitle = "Robert's Station"
        val expectedCoverImageUrl = "https://is1-ssl.mzstatic.com/image/thumb/Features124/v4/7b/1d/f0/7b1df048-0017-8ac0-98c9-735f14849606/mza_7507996640781423701.png/2400x2400bb.jpeg"

        //Act
        val actualMusicPlatformLibraryRadioStation = sut.mapToRadioStation(radioStationDto)

        //Assert
        assertEquals(expectedId, actualMusicPlatformLibraryRadioStation.id)
        assertEquals(expectedTitle, actualMusicPlatformLibraryRadioStation.title)
        assertEquals(expectedCoverImageUrl, actualMusicPlatformLibraryRadioStation.coverArtImageUrl)
    }

}