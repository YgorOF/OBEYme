package com.ygorxkharo.obey.web.data.musiccharts.impl.applemusic

import com.ygorxkharo.obey.web.data.musiccharts.impl.applemusic.model.AppleMusicTopCharts
import com.ygorxkharo.obey.web.data.playlists.integration.MusicPlatformLibraryException
import com.ygorxkharo.obey.web.data.songs.dto.MusicPlatformSongsRequestHandler
import com.ygorxkharo.obey.web.data.songs.linking.sdk.applemusic.dto.model.AppleMusicLibrarySongDto
import com.ygorxkharo.obey.web.data.songs.linking.sdk.applemusic.dto.model.LibrarySongAttributes
import com.ygorxkharo.obey.web.data.songs.linking.sdk.applemusic.dto.model.LibrarySongCollectionDto
import com.ygorxkharo.obey.web.data.songs.linking.sdk.applemusic.dto.model.LibrarySongsRetrievalRequestDto
import com.ygorxkharo.obey.web.data.songs.linking.sdk.applemusic.dto.model.PlaybackAttributes
import com.ygorxkharo.obey.web.domain.musicplatforms.charts.SongChartsRepository
import com.ygorxkharo.obey.web.domain.musicplatforms.charts.model.TopChart
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AppleMusicTopChartsServiceTest {

    private lateinit var sut: AppleMusicTopChartsService
    private lateinit var songsRequestHandler: MusicPlatformSongsRequestHandler<LibrarySongsRetrievalRequestDto, LibrarySongCollectionDto>
    private val songChartsRepository = stubSongChartsRepository()
    private val topChartsCollection = listOf(AppleMusicTopCharts.GLOBAL_100)
    private lateinit var actualArtistAndSongNameCollection: List<String>
    private lateinit var actualTopChartType: TopChart
    private var isSongChartsRepositoryCalled = false
    private val artistAndSongNameCollectionSaveCallback: (TopChart, List<String>) -> Unit = { topChartType, artistAndSongNameCollection ->
        actualTopChartType = topChartType
        actualArtistAndSongNameCollection = artistAndSongNameCollection
    }
    private val accessToken = "ey.2lsjflasjflj2o3h9fehfoijhfkajsdhfao9fh2jfaa"

    @BeforeTest
    fun setup() {
        isSongChartsRepositoryCalled = false
    }

    @Test
    fun test_When_ChartsDataIsAcquiredSuccessfully_Expect_ArtistAndSongNamesToBeSaved() {
        // Arrange
        val artistName = "Test Artist"
        val songName = "Test song name"
        val songCollectionDto = LibrarySongCollectionDto(
            songs = listOf(
                AppleMusicLibrarySongDto(
                    id = "",
                    attributes = LibrarySongAttributes(
                        artistName = artistName,
                        genres = emptyList(),
                        durationInMillis = 200L,
                        title = songName,
                        albumName = songName,
                        playbackAttributes = PlaybackAttributes()
                    )
                )
            )
        )
        val expectedArtistAndSongName = "$artistName-$songName"
        val expectedTopChartType = AppleMusicTopCharts.GLOBAL_100

        songsRequestHandler = stubMusicSongRequestHandler(songResultCollection = songCollectionDto)

        sut = AppleMusicTopChartsService(
            songsRequestHandler = songsRequestHandler,
            topChartsCollection = topChartsCollection,
            songChartsRepository = songChartsRepository
        )

        // Act
        sut.getAllTopCharts(accessToken)

        // Assert
        assertTrue(isSongChartsRepositoryCalled)
        assertEquals(expectedArtistAndSongName, actualArtistAndSongNameCollection.first())
        assertEquals(expectedTopChartType, actualTopChartType)
    }

    @Test
    fun test_When_ChartsDataRequestFails_Expect_NothingHappens() {
        // Arrange
        val errorMessage = "Error getting charts data for top chart type"
        songsRequestHandler = stubMusicSongRequestHandler(errorMessage = errorMessage)

        sut = AppleMusicTopChartsService(
            songsRequestHandler = songsRequestHandler,
            topChartsCollection = topChartsCollection,
            songChartsRepository = songChartsRepository
        )

        // Act
        sut.getAllTopCharts(accessToken)

        // Assert
        assertFalse(isSongChartsRepositoryCalled)
    }

    private fun stubMusicSongRequestHandler(
        errorMessage: String? = null,
        songResultCollection: LibrarySongCollectionDto? = null
    ): MusicPlatformSongsRequestHandler<LibrarySongsRetrievalRequestDto, LibrarySongCollectionDto> {
        return object: MusicPlatformSongsRequestHandler<LibrarySongsRetrievalRequestDto, LibrarySongCollectionDto> {
            override fun handleSongsRetrievalRequest(
                requestPayload: LibrarySongsRetrievalRequestDto,
                onRequestHandled: (LibrarySongCollectionDto) -> Unit,
                onRequestFailed: (Exception) -> Unit,
            ) {

                if(songResultCollection != null) {
                    return onRequestHandled.invoke(songResultCollection)
                }

                if(errorMessage != null) {
                    onRequestFailed.invoke(MusicPlatformLibraryException(errorMessage))
                }
            }
        }
    }

    private fun stubSongChartsRepository(): SongChartsRepository {
        return object: SongChartsRepository {
            override fun saveChart(topChart: TopChart, songAndArtistNamesCollection: List<String>) {
                isSongChartsRepositoryCalled = true
                artistAndSongNameCollectionSaveCallback(topChart, songAndArtistNamesCollection)
            }
        }
    }
}