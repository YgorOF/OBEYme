package com.ygorxkharo.obey.web.data.songs.linking.sdk.applemusic.dto

import com.ygorxkharo.obey.web.data.songs.linking.sdk.applemusic.AppleMusicLibrarySongTestFixture
import com.ygorxkharo.obey.web.data.songs.linking.sdk.applemusic.dto.model.LibrarySongCollectionDto
import com.ygorxkharo.obey.web.data.utils.JsonStringToObjectDeserializer
import kotlin.test.Test
import kotlin.test.assertEquals

class AppleMusicSongMapperTest {

    private val appleMusicCollectionDtoJson = AppleMusicLibrarySongTestFixture.APPLE_MUSIC_LIBRARY_SONG_JSON_RESPONSE
    private val appleMusicCollectionDto = JsonStringToObjectDeserializer.convertFromString(
        LibrarySongCollectionDto.serializer(),
        appleMusicCollectionDtoJson
    )
    private val appleMusicSongDto = appleMusicCollectionDto.songs.first()
    private val CANADIAN_FRENCH_LOCALE = "fr-CA" // yields the following Date format YYYY-mm-dd

    @Test
    fun test_When_ConvertingAnAppleMusicSongDTO_Expect_AMusicPlatformSongToBeReturned() {
        //Arrange
        val sut = AppleMusicSongMapper()
        val expectedId = "i.EYVbDJbumNdr2Q2"
        val expectedSecondaryId = "1088796689"
        val expectedDurationInMillis = 236827L
        val expectedArtistName = "Yemi Alade"
        val expectedAlbumName = "Mama Africa (The Diary of an African Woman)"
        val expectedGenres = listOf("Afro-Pop")
        val expectedSongTitle = "Africa (feat. Sauti Sol)"
        val expectedReleaseDateString = "2016-03-25"

        //Act
        val actualMusicPlatformLibrarySong = sut.mapToLibrarySong(appleMusicSongDto)

        //Assert
        assertEquals(expectedId, actualMusicPlatformLibrarySong.songPlaybackAttribution.id)
        assertEquals(expectedSecondaryId, actualMusicPlatformLibrarySong.songPlaybackAttribution.secondaryId)
        assertEquals(expectedDurationInMillis, actualMusicPlatformLibrarySong.songPlaybackAttribution.durationInMillis)
        assertEquals(expectedArtistName, actualMusicPlatformLibrarySong.performerAttribution.artistName)
        assertEquals(expectedAlbumName, actualMusicPlatformLibrarySong.performerAttribution.albumName)
        assertEquals(expectedGenres.first(), actualMusicPlatformLibrarySong.performerAttribution.genres.first())
        assertEquals(expectedSongTitle, actualMusicPlatformLibrarySong.performerAttribution.songTitle)
        val dateToFrenchLocaleFormat = actualMusicPlatformLibrarySong.publishingAttribution.releaseDateUTC.toLocaleDateString(CANADIAN_FRENCH_LOCALE)
        assertEquals(expectedReleaseDateString, dateToFrenchLocaleFormat)
    }

}