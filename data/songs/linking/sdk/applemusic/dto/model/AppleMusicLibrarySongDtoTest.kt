package com.ygorxkharo.obey.web.data.songs.linking.sdk.applemusic.dto.model

import com.ygorxkharo.obey.web.data.songs.linking.sdk.applemusic.AppleMusicLibrarySongTestFixture
import com.ygorxkharo.obey.web.data.utils.JsonStringToObjectDeserializer
import kotlin.test.Test
import kotlin.test.assertEquals

class AppleMusicLibrarySongDtoTest {

    private val appleMusicLibrarySongJson = AppleMusicLibrarySongTestFixture.APPLE_MUSIC_LIBRARY_SONG_JSON_RESPONSE

    @Test
    fun test_When_SerializingLibrarySongCollectionDTO_Expect_NextDataAndMetaToBeSet() {
        //Arrange
        val expectedTotalSongCount = 1
        val expectedSongCollectionSize = 1
        val expectedNextUrl = "/v1/me/library/songs?offset=2"

        //Act
        val actualAppleMusicLibrarySong = JsonStringToObjectDeserializer.convertFromString(LibrarySongCollectionDto.serializer(), appleMusicLibrarySongJson)

        //Assert
        assertEquals(expectedTotalSongCount, actualAppleMusicLibrarySong.songCollectionAudit?.totalCount)
        assertEquals(expectedNextUrl, actualAppleMusicLibrarySong.nextCollectionResultUrl)
        assertEquals(expectedSongCollectionSize, actualAppleMusicLibrarySong.songs.size)
    }

    @Test
    fun test_When_SerializingLibrarySongDTO_Expect_IDAndAttributesToBeSet() {
        //Arrange
        val expectedLibrarySongId = "i.EYVbDJbumNdr2Q2"
        val expectedArtWorkWidth = 1200
        val expectedArtWorkHeight = 1200
        val expectedArtWorkUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music124/v4/0b/39/94/0b39941c-daf3-efc6-a778-e025a3ecbab1/5060438970110.jpg/{w}x{h}bb.jpeg"
        val expectedArtistName = "Yemi Alade"
        val expectedGenreName = "Afro-Pop"
        val expectedDurationInMillis = 236827L
        val expectedReleaseDate = "2016-03-25"
        val expectedSongTitle = "Africa (feat. Sauti Sol)"
        val expectedAlbumName = "Mama Africa (The Diary of an African Woman)"
        val expectedCatalogId = "1088796689"

        //Act
        val actualAppleMusicLibrarySong = JsonStringToObjectDeserializer.convertFromString(LibrarySongCollectionDto.serializer(), appleMusicLibrarySongJson)

        //Assert
        assertEquals(expectedLibrarySongId, actualAppleMusicLibrarySong.songs.first().id)

        val firstSongAttributes = actualAppleMusicLibrarySong.songs.first().attributes
        assertEquals(expectedArtWorkWidth, firstSongAttributes?.artWork?.width)
        assertEquals(expectedArtWorkHeight, firstSongAttributes?.artWork?.height)
        assertEquals(expectedArtWorkUrl, firstSongAttributes?.artWork?.coverImageUrl)
        assertEquals(expectedArtistName, firstSongAttributes?.artistName)
        assertEquals(expectedGenreName, firstSongAttributes?.genres?.first())
        assertEquals(expectedDurationInMillis, firstSongAttributes?.durationInMillis)
        assertEquals(expectedReleaseDate, firstSongAttributes?.releaseDate)
        assertEquals(expectedSongTitle, firstSongAttributes?.title)
        assertEquals(expectedAlbumName, firstSongAttributes?.albumName)
        assertEquals(expectedCatalogId, firstSongAttributes?.playbackAttributes?.catalogId)
    }

}