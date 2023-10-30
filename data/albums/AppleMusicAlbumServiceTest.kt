package com.ygorxkharo.obey.web.data.albums

import com.ygorxkharo.obey.web.data.common.browser.http.HttpRequestHandler
import com.ygorxkharo.obey.web.data.playlists.integration.MusicPlatformLibraryException
import com.ygorxkharo.obey.web.domain.recordlabels.model.AlbumRequest
import com.ygorxkharo.obey.web.domain.recordlabels.model.AlbumResult
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AppleMusicAlbumServiceTest {

    private val albumId = "153943493"
    private val accessToken = "AF.fajkhfn2lkjhfkajhdfads9fy9aohfajlksdhf99oi2hjkdas9h9asfhaf"
    private val albumRequest = AlbumRequest(albumCatalogId = albumId, accessToken = accessToken)
    private var actualAlbumResult: Result<AlbumResult>? = null
    private val onCompleteRequest: (Result<AlbumResult>) -> Unit = { result ->
        actualAlbumResult = result
    }

    @BeforeTest
    fun setup() {
        actualAlbumResult = null
    }

    @Test
    fun test_When_SuccessfullyFetchedAlbumInformation_Expect_CopyrightDescriptionToBeContainedInAlbumResult() {
        //Arrange
        val expectedCopyrightDescription = "℗ 2022 Falco Records a division of Warner Recordings, Inc."
        val albumRequestHandler = stubAlbumHttpRequestHandler(copyrightDescription = expectedCopyrightDescription)
        val sut = AppleMusicAlbumService(albumHttpRequestHandler = albumRequestHandler)

        //Act
        sut.getAlbumById(albumRequest, onCompleteRequest)

        //Assert
        assertTrue(actualAlbumResult?.isSuccess == true)
        assertEquals(expectedCopyrightDescription, actualAlbumResult?.getOrNull()?.copyrightDescription)
    }

    @Test
    fun test_When_AlbumInformationRequestFails_Expect_ExceptionIsContainedAlbumResult() {
        //Arrange
        val expectedErrorMessage = "No album information available"
        val albumRequestHandler = stubAlbumHttpRequestHandler(errorMessage = expectedErrorMessage)
        val sut = AppleMusicAlbumService(albumHttpRequestHandler = albumRequestHandler)

        //Act
        sut.getAlbumById(albumRequest, onCompleteRequest)

        //Assert
        assertTrue(actualAlbumResult?.isFailure == true)
        assertEquals(expectedErrorMessage, actualAlbumResult?.exceptionOrNull()?.message)
    }

    private fun stubAlbumHttpRequestHandler(
        copyrightDescription: String? = null,
        errorMessage: String? = null
    ): HttpRequestHandler<AlbumRequest, AlbumResult> {
        return object: HttpRequestHandler<AlbumRequest, AlbumResult> {
            override fun handleRetrievalRequest(
                requestPayload: AlbumRequest,
                onRequestHandled: (AlbumResult) -> Unit,
                onRequestFailed: (Exception) -> Unit,
            ) {
                if(copyrightDescription != null) {
                    return onRequestHandled.invoke(AlbumResult(copyrightDescription = copyrightDescription))
                }

                if(errorMessage != null) {
                    return onRequestFailed.invoke(MusicPlatformLibraryException(errorMessage))
                }
            }
        }
    }
}