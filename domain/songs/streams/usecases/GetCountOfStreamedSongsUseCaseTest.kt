package com.ygorxkharo.obey.web.domain.songs.streams.usecases

import com.ygorxkharo.obey.web.data.playlists.integration.MusicPlatformLibraryException
import com.ygorxkharo.obey.web.domain.common.Failure
import com.ygorxkharo.obey.web.domain.common.Result
import com.ygorxkharo.obey.web.domain.common.Success
import com.ygorxkharo.obey.web.domain.musicplatforms.songs.streams.StreamedSongsService
import com.ygorxkharo.obey.web.domain.musicplatforms.songs.streams.model.StreamedSongsCountRequest
import com.ygorxkharo.obey.web.domain.musicplatforms.songs.streams.usecases.GetCountOfStreamedSongsUseCase
import kotlin.js.Date
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetCountOfStreamedSongsUseCaseTest {

    private lateinit var actualResult: Result<Int>
    private val onCompleteCallback: (Result<Int>) -> Unit = {
        actualResult = it
    }
    private val streamedSongsCountRequest = StreamedSongsCountRequest(
        userId = "test_user_2232",
        asOfDate = Date()
    )

    @Test
    fun test_When_InvokingUseCaseWithSuccess_Expect_SuccessResultToBeReceivedOnComplete() {
        //Arrange
        val expectedStreamedSongsCount = 134
        val streamedSongsService = stubStreamedSongsService(totalStreamedSongs =  expectedStreamedSongsCount)
        val sut = GetCountOfStreamedSongsUseCase(streamedSongsService)

        //Act
        sut.invoke(streamedSongsCountRequest, onCompleteCallback)

        //Assert
        assertTrue(actualResult is Success)
        assertEquals(expectedStreamedSongsCount, (actualResult as Success).payload)
    }

    @Test
    fun test_When_InvokingUseCaseWithFailure_Expect_FailureResultToBeReceivedOnComplete() {
        //Arrange
        val expectedErrorMessage = "Unable to get linked playlists"
        val streamedSongsService = stubStreamedSongsService(errorMessage = expectedErrorMessage)
        val sut = GetCountOfStreamedSongsUseCase(streamedSongsService)

        //Act
        sut.invoke(streamedSongsCountRequest, onCompleteCallback)

        //Assert
        assertTrue(actualResult is Failure)
        assertEquals(expectedErrorMessage, (actualResult as Failure).error.message)
    }

    private fun stubStreamedSongsService(totalStreamedSongs: Int? = null, errorMessage: String? = null): StreamedSongsService {
        return object: StreamedSongsService {

            override fun getTotalCountOfStreamedSongsInApp(
                streamedSongsCountRequest: StreamedSongsCountRequest,
                onGetStreamedSongsCount: (Result<Int>) -> Unit,
            ) {
                if(totalStreamedSongs != null) {
                    return onGetStreamedSongsCount.invoke(Success(totalStreamedSongs))
                }

                if(errorMessage != null) {
                    val libraryError = MusicPlatformLibraryException(errorMessage)
                    onGetStreamedSongsCount(Failure(libraryError))
                }
            }
        }
    }

}