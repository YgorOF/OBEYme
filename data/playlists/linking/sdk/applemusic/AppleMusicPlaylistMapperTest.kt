package com.ygorxkharo.obey.web.data.playlists.linking.sdk.applemusic

import com.ygorxkharo.obey.web.data.playlists.integration.sdk.applemusic.dto.AppleMusicLibraryPlaylistMapper
import com.ygorxkharo.obey.web.data.playlists.integration.sdk.applemusic.dto.model.AppleMusicPlaylistDto
import com.ygorxkharo.obey.web.data.utils.JsonStringToObjectDeserializer
import kotlin.js.Date
import kotlin.test.Test
import kotlin.test.assertEquals

class AppleMusicPlaylistMapperTest {

    private val sut = AppleMusicLibraryPlaylistMapper()

    @Test
    fun test_When_PlaylistEntityIsConverted_Expect_AMusicPlatformPlaylistToBeReturned() {
        //Arrange
        val expectedId = "p.oOzA3XOclW6AZ65"
        val expectedPlaylistTitle = "Army Drill Sounds"
        val expectedCoverImageUrl = "https://example.com"
        val expectedPublishedDate = Date("2021-06-30T09:35:21Z")
        val playlistEntityJson = AppleMusicPlaylistTestFixture.PLAYLIST_JSON_PAYLOAD
        val playlistEntity = JsonStringToObjectDeserializer.convertFromString(AppleMusicPlaylistDto.serializer(), playlistEntityJson)

        //Act
        val actualPlaylist = sut.mapToPlaylist(playlistEntity)

        //Assert
        assertEquals(expectedId, actualPlaylist.id)
        assertEquals(expectedPlaylistTitle, actualPlaylist.title)
        assertEquals(expectedCoverImageUrl, actualPlaylist.coverArtImageUrl)
        assertEquals(expectedPublishedDate.toString(), actualPlaylist.publishedDateUTC.toString())
    }

    @Test
    fun test_When_PlaylistEntityWithNoCoverImageIsConverted_Expect_AMusicPlatformPlaylistWithDefaultPlaceholderToBeReturned() {
        //Arrange
        val expectedCoverImageUrl = "images/backgrounds/bg_apple_music_playlist_cover_image_placeholder.jpg"
        val playlistEntityJson = AppleMusicPlaylistTestFixture.PLAYLIST_NO_COVER_ART_JSON_PAYLOAD
        val playlistEntity = JsonStringToObjectDeserializer.convertFromString(AppleMusicPlaylistDto.serializer(), playlistEntityJson)

        //Act
        val actualPlaylist = sut.mapToPlaylist(playlistEntity)

        //Assert
        assertEquals(expectedCoverImageUrl, actualPlaylist.coverArtImageUrl)
    }
}