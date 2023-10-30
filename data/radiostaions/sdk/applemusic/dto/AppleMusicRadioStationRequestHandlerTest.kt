package com.ygorxkharo.obey.web.data.radiostaions.sdk.applemusic.dto

import com.ygorxkharo.obey.web.data.common.browser.http.HttpClient
import com.ygorxkharo.obey.web.data.playlists.integration.MusicPlatformLibraryException
import com.ygorxkharo.obey.web.data.radiostations.sdk.applemusic.dto.AppleMusicRadioStationRequestHandler
import com.ygorxkharo.obey.web.data.radiostations.sdk.applemusic.dto.model.RadioStationCollectionDto
import com.ygorxkharo.obey.web.data.radiostations.sdk.applemusic.dto.model.RadioStationRetrievalRequestDto
import com.ygorxkharo.obey.web.data.utils.JsonStringToObjectDeserializer
import com.ygorxkharo.obey.web.domain.common.Failure
import com.ygorxkharo.obey.web.domain.common.Result
import com.ygorxkharo.obey.web.domain.common.Success
import org.w3c.fetch.RequestInit
import kotlin.test.Test
import kotlin.test.assertEquals

class AppleMusicRadioStationRequestHandlerTest {

    private val developerToken = "ey.slfajkfjlasjdflkajdflsaj"
    private lateinit var actualRadioStationCollectionDto: RadioStationCollectionDto
    private val onRequestHandled: (RadioStationCollectionDto)  -> Unit = {
        actualRadioStationCollectionDto = it
    }

    private lateinit var actualRadioStationRetrievalFailureException: Exception
    private val onRequestFailed: (Exception)  -> Unit = {
        actualRadioStationRetrievalFailureException = it
    }

    private val musicPlatformAccessToken = "ey.lvcjalfjlkajflkj2lkjlajflkajdfa"
    private val countryCode = "za"
    private val radioStationRequestDto = RadioStationRetrievalRequestDto(
        accessToken =  musicPlatformAccessToken,
        countryCode = countryCode
    )

    @Test
    fun test_When_RequestIsSuccessful_Expect_onRequestHandledTriggeredWithSuccess() {
        //Arrange
        val radioStationCollectionDto = AppleMusicRadioStationTestFixture.RADIO_STATION_COLLECTION_DTO_PAYLOAD
        val expectedRadioStationCollectionDto = JsonStringToObjectDeserializer.convertFromString(
            RadioStationCollectionDto.serializer(),
            radioStationCollectionDto
        )

        val httpClient = stubHttpClient(responsePayload = radioStationCollectionDto)
        val sut = AppleMusicRadioStationRequestHandler(httpClient, developerToken)

        //Act
        sut.handleRadioStationRetrievalRequest(radioStationRequestDto, onRequestHandled, onRequestFailed)

        //Assert
        assertEquals(expectedRadioStationCollectionDto, actualRadioStationCollectionDto)
    }

    @Test
    fun test_When_RequestFails_Expect_onRequestFailedToTriggeredWithFailure() {
        //Arrange
        val expectedRadioStationRetrievalResponseErrorMessage = "Error getting radio stations"
        val httpClient = stubHttpClient(errorMessage = expectedRadioStationRetrievalResponseErrorMessage)
        val sut = AppleMusicRadioStationRequestHandler(httpClient, developerToken)

        //Act
        sut.handleRadioStationRetrievalRequest(radioStationRequestDto, onRequestHandled, onRequestFailed)

        //Assert
        assertEquals(expectedRadioStationRetrievalResponseErrorMessage, actualRadioStationRetrievalFailureException.message)
    }

    private fun stubHttpClient(
        responsePayload: String? = null,
        errorMessage: String? = null
    ): HttpClient<RequestInit, String> {
        return object: HttpClient<RequestInit, String> {
            override fun handleRequest(
                destinationUrl: String,
                requestOptions: RequestInit,
                onRequestHandled: (Result<String>) -> Unit
            ) {
                if(responsePayload != null) {
                    onRequestHandled(Success(responsePayload))
                }

                if(errorMessage != null) {
                    val failureResult = Failure(MusicPlatformLibraryException(errorMessage))
                    onRequestHandled(failureResult)
                }
            }
        }
    }

}