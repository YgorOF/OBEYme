package com.ygorxkharo.obey.web.domain.playlists.linking.usecases

import com.ygorxkharo.obey.web.data.playlists.integration.MusicPlatformLibraryException
import com.ygorxkharo.obey.web.domain.common.Failure
import com.ygorxkharo.obey.web.domain.common.Result
import com.ygorxkharo.obey.web.domain.common.Success
import com.ygorxkharo.obey.web.domain.musicplatforms.playlists.linking.LinkedPlaylistsService
import com.ygorxkharo.obey.web.domain.musicplatforms.playlists.linking.model.LinkedPlaylistsCountRequest
import com.ygorxkharo.obey.web.domain.musicplatforms.playlists.linking.usecases.GetCountOfLinkedPlaylistsUseCase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetCountOfLinkedPlaylistsUseCaseTest {

    private lateinit var actualResult: Result<Int>
    private val onCompleteCallback: (Result<Int>) -> Unit = {
        actualResult = it
    }
    private val linkedPlaylistsCountRequest = LinkedPlaylistsCountRequest(userId = "test_user_2232")

    @Test
    fun test_When_InvokingUseCaseWithSuccess_Expect_SuccessResultToBeReceivedOnComplete() {
        //Arrange
        val expectedPlaylistCount = 4
        val linkedPlaylistsService = stubLinkedPlaylistsService(totalPlaylistCount =  expectedPlaylistCount)
        val sut = GetCountOfLinkedPlaylistsUseCase(linkedPlaylistsService)

        //Act
        sut.invoke(linkedPlaylistsCountRequest, onCompleteCallback)

        //Assert
        assertTrue(actualResult is Success)
        assertEquals(expectedPlaylistCount, (actualResult as Success).payload)
    }

    @Test
    fun test_When_InvokingUseCaseWithFailure_Expect_FailureResultToBeReceivedOnComplete() {
        //Arrange
        val expectedErrorMessage = "Unable to get linked playlists"
        val linkedPlaylistsService = stubLinkedPlaylistsService(errorMessage = expectedErrorMessage)
        val sut = GetCountOfLinkedPlaylistsUseCase(linkedPlaylistsService)

        //Act
        sut.invoke(linkedPlaylistsCountRequest, onCompleteCallback)

        //Assert
        assertTrue(actualResult is Failure)
        assertEquals(expectedErrorMessage, (actualResult as Failure).error.message)
    }

    private fun stubLinkedPlaylistsService(totalPlaylistCount: Int? = null, errorMessage: String? = null): LinkedPlaylistsService {
        return object: LinkedPlaylistsService {
            override fun getTotalCountOfLinkedPlaylists(
                linkedPlaylistCountRequest: LinkedPlaylistsCountRequest,
                onGetTotalPlaylists: (Result<Int>) -> Unit,
            ) {
                if(totalPlaylistCount != null) {
                    return onGetTotalPlaylists.invoke(Success(totalPlaylistCount))
                }

                if(errorMessage != null) {
                    val libraryError = MusicPlatformLibraryException(errorMessage)
                    onGetTotalPlaylists(Failure(libraryError))
                }
            }
        }
    }

}