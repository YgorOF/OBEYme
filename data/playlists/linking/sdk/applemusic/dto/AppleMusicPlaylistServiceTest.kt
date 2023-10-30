package com.ygorxkharo.obey.web.data.playlists.linking.sdk.applemusic.dto

import com.ygorxkharo.obey.web.data.common.browser.http.HttpRequestHandler
import com.ygorxkharo.obey.web.data.platforms.MusicPlatform
import com.ygorxkharo.obey.web.data.playlists.integration.MusicPlatformLibraryException
import com.ygorxkharo.obey.web.data.playlists.integration.dto.MusicPlatformPlaylistMapper
import com.ygorxkharo.obey.web.data.playlists.integration.dto.MusicPlatformPlaylistRequestHandler
import com.ygorxkharo.obey.web.data.playlists.integration.sdk.applemusic.dto.AppleMusicLibraryPlaylistMapper
import com.ygorxkharo.obey.web.data.playlists.integration.sdk.applemusic.dto.AppleMusicPlaylistService
import com.ygorxkharo.obey.web.data.playlists.integration.sdk.applemusic.dto.model.PlaylistCollectionDto
import com.ygorxkharo.obey.web.data.playlists.linking.recommendations.impl.applemusic.dto.AppleMusicMediaResource
import com.ygorxkharo.obey.web.data.playlists.linking.recommendations.impl.applemusic.dto.AppleMusicResourceRelationshipsContents
import com.ygorxkharo.obey.web.data.playlists.linking.recommendations.impl.applemusic.dto.RecommendationDto
import com.ygorxkharo.obey.web.data.playlists.linking.recommendations.impl.applemusic.dto.RecommendationsDto
import com.ygorxkharo.obey.web.data.playlists.linking.recommendations.impl.applemusic.dto.RecommendationsRelationshipDto
import com.ygorxkharo.obey.web.data.playlists.linking.sdk.applemusic.AppleMusicPlaylistTestFixture
import com.ygorxkharo.obey.web.data.radiostaions.sdk.applemusic.dto.AppleMusicRadioStationTestFixture
import com.ygorxkharo.obey.web.data.radiostations.dto.MusicPlatformRadioStationMapper
import com.ygorxkharo.obey.web.data.radiostations.dto.MusicPlatformRadioStationRequestHandler
import com.ygorxkharo.obey.web.data.radiostations.sdk.applemusic.dto.model.RadioStationCollectionDto
import com.ygorxkharo.obey.web.data.radiostations.sdk.applemusic.dto.model.RadioStationDto
import com.ygorxkharo.obey.web.data.radiostations.sdk.applemusic.dto.model.RadioStationRetrievalRequestDto
import com.ygorxkharo.obey.web.data.utils.JsonStringToObjectDeserializer
import com.ygorxkharo.obey.web.domain.common.Failure
import com.ygorxkharo.obey.web.domain.common.Result
import com.ygorxkharo.obey.web.domain.common.Success
import com.ygorxkharo.obey.web.domain.musicplatforms.playlists.linking.model.MusicPlatformPlaylist
import com.ygorxkharo.obey.web.domain.musicplatforms.playlists.linking.model.PlaylistCategory
import com.ygorxkharo.obey.web.domain.musicplatforms.playlists.linking.model.PlaylistRetrievalRequest
import com.ygorxkharo.obey.web.domain.musicplatforms.playlists.linking.model.PlaylistRetrievalResponse
import com.ygorxkharo.obey.web.domain.musicplatforms.radiostations.model.MusicPlatformRadioStation
import com.ygorxkharo.obey.web.domain.musicplatforms.radiostations.model.RadioStationRetrievalRequest
import com.ygorxkharo.obey.web.domain.musicplatforms.radiostations.model.RadioStationRetrievalResponse
import kotlin.js.Date
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AppleMusicPlaylistServiceTest {

    private val musicPlatformPlaylistMapper = AppleMusicLibraryPlaylistMapper()
    private val appleMusicMusicToken = "AF.dsfj2rlkljlsfjalfjaljfaldjfadfasf"

    private lateinit var actualPlaylistRetrievalResult: Result<PlaylistRetrievalResponse>
    private val onComplete: (Result<PlaylistRetrievalResponse>) -> Unit = {
        actualPlaylistRetrievalResult = it
    }

    private val expectedPlaylistId = "p.oOzA3XOclW6AZ65"
    private val expectedPlaylistTitle = "Army Drill Sounds"
    private val expectedPlaylistPublishDate = Date("2021-06-30T09:35:21Z")
    private val expectedCoverImageUrl = "https://example.com"
    private val expectedPlaylistCount = 1
    private val accessToken = "y.3rojw9jasoifjaofjalkfjalkdfj"
    private val countryCode = "za"
    private val expectedRadioStationId = "s99.3sfsjaf"
    private val expectedRadioStationTitle = "test_radio_station"
    private val expectedRadioStationImageUrl = "https://example.png"
    private val recommendationsDto = JsonStringToObjectDeserializer.convertFromString(
        RecommendationsDto.serializer(),
        AppleMusicPlaylistTestFixture.RECOMMENDATIONS_DTO_STRING
    )
    private val radioStationRetrievalRequest = RadioStationRetrievalRequest(
        musicPlatform = "test_music_platform",
        musicPlatformAccessToken = accessToken,
        countryCode = countryCode
    )

    private lateinit var actualRadioStationResult: Result<RadioStationRetrievalResponse>
    private val onCompleteRadioStationRequest: (Result<RadioStationRetrievalResponse>) -> Unit = {
        actualRadioStationResult = it
    }

    private val playlistRetrievalRequest = PlaylistRetrievalRequest(
        musicPlatformAccessToken = appleMusicMusicToken,
        musicPlatform = MusicPlatform.APPLE_MUSIC.value,
        playlistCountOffset = 0
    )

    @Test
    fun test_When_ServiceGetsPlaylistCollection_Expect_OnCompleteToTriggerWithSuccess() {
        //Arrange
        val expectedPlaylistCount = 2
        val responsePayload = AppleMusicPlaylistTestFixture.MIXED_PLAYLIST_JSON_PAYLOAD
        val responseErrorMessage = null
        val requestHandler = stubPlaylistRequestHandler(responsePayload, responseErrorMessage)
        val radioStationRequestHandler = stubRadioStationRequestHandler(radioStationCollectionDTOJsonString = responsePayload)
        val musicPlatformRadioStationMapper = stubRadioStationMapper()
        val recommendationRequestHandler = stubRecommendationRequestHandler()
        val recommendationsMapper = stubRecommendationMapper()
        val sut = AppleMusicPlaylistService(
            requestHandler,
            musicPlatformPlaylistMapper,
            radioStationRequestHandler,
            musicPlatformRadioStationMapper,
            recommendationRequestHandler = recommendationRequestHandler,
            recommendationsMapper = recommendationsMapper
        )

        //Act
        sut.getPlaylists(playlistRetrievalRequest, onComplete)

        //Assert
        assertTrue(actualPlaylistRetrievalResult is Success)
        val successfulPlaylistRetrievalResult = actualPlaylistRetrievalResult as Success
        assertEquals(expectedPlaylistCount, successfulPlaylistRetrievalResult.payload.playlists.size)

        val firstPlaylist = successfulPlaylistRetrievalResult.payload.playlists.first()
        assertEquals(expectedPlaylistId, firstPlaylist.id)
        assertEquals(expectedCoverImageUrl, firstPlaylist.coverArtImageUrl)
        assertEquals(expectedPlaylistTitle, firstPlaylist.title)
        assertEquals(expectedPlaylistPublishDate.toString(), firstPlaylist.publishedDateUTC.toString())
    }

    @Test
    fun test_When_ServiceFailsToGetPlaylists_Expect_OnCompleteToTriggerWithFailure() {
        //Arrange
        val responsePayload = null
        val responseErrorMessage = "Failed to get playlists from Apple Music"
        val requestHandler = stubPlaylistRequestHandler(responsePayload, responseErrorMessage)
        val radioStationRequestHandler = stubRadioStationRequestHandler()
        val musicPlatformRadioStationMapper = stubRadioStationMapper()
        val recommendationRequestHandler = stubRecommendationRequestHandler()
        val recommendationMapper = stubRecommendationMapper()
        val sut = AppleMusicPlaylistService(
            requestHandler,
            musicPlatformPlaylistMapper,
            radioStationRequestHandler,
            musicPlatformRadioStationMapper,
            recommendationRequestHandler,
            recommendationMapper
        )

        //Act
        sut.getPlaylists(playlistRetrievalRequest, onComplete)

        //Assert
        assertTrue(actualPlaylistRetrievalResult is Failure)
        val failurePlaylistRetrievalResult = actualPlaylistRetrievalResult as Failure
        assertEquals(responseErrorMessage, failurePlaylistRetrievalResult.error.message)
    }

    @Test
    fun test_When_ServiceGetsRadioStationCollection_Expect_OnCompleteToTriggerWithSuccess() {
        //Arrange
        val responsePayload = AppleMusicRadioStationTestFixture.RADIO_STATION_COLLECTION_DTO_PAYLOAD
        val requestHandler = stubPlaylistRequestHandler()
        val radioStationRequestHandler = stubRadioStationRequestHandler(radioStationCollectionDTOJsonString = responsePayload)
        val musicPlatformRadioStationMapper = stubRadioStationMapper()
        val recommendationRequestHandler = stubRecommendationRequestHandler()
        val recommendationMapper = stubRecommendationMapper()
        val sut = AppleMusicPlaylistService(
            requestHandler,
            musicPlatformPlaylistMapper,
            radioStationRequestHandler,
            musicPlatformRadioStationMapper,
            recommendationRequestHandler,
            recommendationMapper
        )

        //Act
        sut.getUserPersonalRadioStation(radioStationRetrievalRequest, onCompleteRadioStationRequest)

        //Assert
        assertTrue(actualRadioStationResult is Success)
        val successResult = actualRadioStationResult as Success
        assertEquals(expectedRadioStationId, successResult.payload.radioStation.id)
        assertEquals(expectedRadioStationTitle, successResult.payload.radioStation.title)
        assertEquals(expectedRadioStationImageUrl, successResult.payload.radioStation.coverArtImageUrl)
    }

    @Test
    fun test_When_ServiceFailsToGetRadioStations_Expect_OnCompleteToTriggerWithFailure() {
        //Arrange
        val responseErrorMessage = "Failed to get radio stations from Apple Music"
        val requestHandler = stubPlaylistRequestHandler()
        val radioStationRequestHandler = stubRadioStationRequestHandler(responseErrorMessage = responseErrorMessage)
        val musicPlatformRadioStationMapper = stubRadioStationMapper()
        val recommendationRequestHandler = stubRecommendationRequestHandler()
        val recommendationMapper = stubRecommendationMapper()
        val sut = AppleMusicPlaylistService(
            requestHandler,
            musicPlatformPlaylistMapper,
            radioStationRequestHandler,
            musicPlatformRadioStationMapper,
            recommendationRequestHandler,
            recommendationMapper
        )

        //Act
        sut.getUserPersonalRadioStation(radioStationRetrievalRequest, onCompleteRadioStationRequest)

        //Assert
        assertTrue(actualRadioStationResult is Failure)
        val failureResult = actualRadioStationResult as Failure
        assertEquals(responseErrorMessage, failureResult.error.message)
    }

    @Test
    fun test_When_ServiceGetsPlaylistCollectionContainingAlbums_Expect_OnCompleteToTriggerWithGenresAvailable() {
        //Arrange
        val expectedPlaylistCount = 2
        val requestHandler = stubPlaylistRequestHandler()
        val radioStationRequestHandler = stubRadioStationRequestHandler()
        val musicPlatformRadioStationMapper = stubRadioStationMapper()
        val recommendationRequestHandler = stubRecommendationRequestHandler(recommendationsDto)
        val recommendationsMapper = stubRecommendationMapper()
        val sut = AppleMusicPlaylistService(
            requestHandler,
            musicPlatformPlaylistMapper,
            radioStationRequestHandler,
            musicPlatformRadioStationMapper,
            recommendationRequestHandler = recommendationRequestHandler,
            recommendationsMapper = recommendationsMapper
        )

        //Act
        sut.getUserRecommendations(playlistRetrievalRequest, onComplete)

        //Assert
        assertTrue(actualPlaylistRetrievalResult is Success)
        val successfulPlaylistRetrievalResult = actualPlaylistRetrievalResult as Success
        assertEquals(expectedPlaylistCount, successfulPlaylistRetrievalResult.payload.playlists.size)

        val firstPlaylist = successfulPlaylistRetrievalResult.payload.playlists.first()
        assertTrue(firstPlaylist.genres != null)
    }

    private fun stubPlaylistRequestHandler(
        playlistCollectionDtoJsonString: String? = null,
        responseErrorMessage: String? = null
    ): MusicPlatformPlaylistRequestHandler<PlaylistRetrievalRequest, PlaylistCollectionDto>  {

        return object: MusicPlatformPlaylistRequestHandler<PlaylistRetrievalRequest, PlaylistCollectionDto> {
            override fun handlePlaylistRetrievalRequest(
                playlistRequestPayload: PlaylistRetrievalRequest,
                onRequestHandled: (PlaylistCollectionDto) -> Unit,
                onRequestFailed: (Exception) -> Unit
            ) {
                if(playlistCollectionDtoJsonString != null) {
                    val playlistCollectionDto = JsonStringToObjectDeserializer.convertFromString(
                        PlaylistCollectionDto.serializer(),
                        playlistCollectionDtoJsonString
                    )
                    onRequestHandled.invoke(playlistCollectionDto)
                }

                if(responseErrorMessage != null) {
                    onRequestFailed(MusicPlatformLibraryException(responseErrorMessage))
                }
            }
        }
    }

    private fun stubRadioStationRequestHandler(
        radioStationCollectionDTOJsonString: String? = null,
        responseErrorMessage: String? = null
    ): MusicPlatformRadioStationRequestHandler<RadioStationRetrievalRequestDto, RadioStationCollectionDto> {
        return object: MusicPlatformRadioStationRequestHandler<RadioStationRetrievalRequestDto, RadioStationCollectionDto> {
            override fun handleRadioStationRetrievalRequest(
                radioStationRetrievalRequest: RadioStationRetrievalRequestDto,
                onRequestHandled: (RadioStationCollectionDto) -> Unit,
                onRequestFailed: (Exception) -> Unit,
            ) {
                if(radioStationCollectionDTOJsonString != null) {
                    val playlistCollectionDto = JsonStringToObjectDeserializer.convertFromString(
                        RadioStationCollectionDto.serializer(),
                        radioStationCollectionDTOJsonString
                    )
                    onRequestHandled.invoke(playlistCollectionDto)
                }

                if(responseErrorMessage != null) {
                    onRequestFailed(MusicPlatformLibraryException(responseErrorMessage))
                }
            }
        }
    }

    private fun stubRadioStationMapper(): MusicPlatformRadioStationMapper<RadioStationDto> {
        return object: MusicPlatformRadioStationMapper<RadioStationDto> {
            override fun mapToRadioStation(thirdPartyRadioStation: RadioStationDto): MusicPlatformRadioStation {
                return MusicPlatformRadioStation(
                    id = expectedRadioStationId,
                    coverArtImageUrl = expectedRadioStationImageUrl,
                    title = expectedRadioStationTitle
                )
            }
        }
    }

    private fun stubRecommendationRequestHandler(
        resultPayload: RecommendationsDto? = null,
        errorMessage: String? = null
    ): HttpRequestHandler<PlaylistRetrievalRequest, RecommendationsDto> {
        return object: HttpRequestHandler<PlaylistRetrievalRequest, RecommendationsDto> {
            override fun handleRetrievalRequest(
                requestPayload: PlaylistRetrievalRequest,
                onRequestHandled: (RecommendationsDto) -> Unit,
                onRequestFailed: (Exception) -> Unit,
            ) {
                if(resultPayload != null) {
                    return onRequestHandled.invoke(resultPayload)
                }

                if(errorMessage != null) {
                    onRequestFailed.invoke(MusicPlatformLibraryException(errorMessage))
                }
            }
        }
    }

    private fun stubRecommendationMapper(): MusicPlatformPlaylistMapper<AppleMusicMediaResource> {
        return object: MusicPlatformPlaylistMapper<AppleMusicMediaResource> {
            override fun mapToPlaylist(thirdPartyPlatformPlaylist: AppleMusicMediaResource): MusicPlatformPlaylist {
                return MusicPlatformPlaylist(
                    id = "",
                    coverArtImageUrl = "",
                    title = "",
                    publishedDateUTC = Date(),
                    playlistType = PlaylistCategory.PLAYLIST_WITH_NO_BACKFILLED_COVER,
                    genres = thirdPartyPlatformPlaylist.attributes?.genres
                )
            }
        }
    }
}