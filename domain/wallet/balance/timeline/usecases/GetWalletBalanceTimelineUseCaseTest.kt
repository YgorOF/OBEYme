package com.ygorxkharo.obey.web.domain.wallet.balance.timeline.usecases

import com.ygorxkharo.obey.web.domain.common.Failure
import com.ygorxkharo.obey.web.domain.common.Result
import com.ygorxkharo.obey.web.domain.common.Success
import com.ygorxkharo.obey.web.domain.wallet.CuratorWalletException
import com.ygorxkharo.obey.web.domain.wallet.balance.WalletBalanceService
import com.ygorxkharo.obey.web.domain.wallet.balance.model.WalletBalanceRequest
import com.ygorxkharo.obey.web.domain.wallet.balance.model.WalletBalanceResult
import com.ygorxkharo.obey.web.domain.wallet.balance.timeline.model.TimelineDateRange
import com.ygorxkharo.obey.web.domain.wallet.balance.timeline.model.WalletBalanceTimeline
import com.ygorxkharo.obey.web.domain.wallet.balance.timeline.model.WalletBalanceTimelineRequest
import kotlin.js.Date
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetWalletBalanceTimelineUseCaseTest {

    private lateinit var actualWalletBalanceRequest: WalletBalanceTimelineRequest
    private lateinit var actualResult: Result<WalletBalanceTimeline>
    private val onCompleteCallback: (Result<WalletBalanceTimeline>) -> Unit = {
        actualResult = it
    }

    private val walletBalanceTimelineRequest = WalletBalanceTimelineRequest(
        userId = "test_user_2232",
        TimelineDateRange(
            from = Date(),
            to = Date()
        )
    )

    private val expectedWalletBalanceTimeline = WalletBalanceTimeline(
        timelineDateRange = TimelineDateRange(
            from = Date(),
            to = Date()
        ),
        walletBalances = emptyList()
    )

    @Test
    fun test_When_InvokingUseCase_Expect_WalletBalanceTimelineRequestToBePassedToWalletBalanceService() {
        //Arrange
        val walletBalanceService = stubWalletBalanceService()
        val sut = GetWalletBalanceTimelineUseCase(walletBalanceService)

        //Act
        sut.invoke(walletBalanceTimelineRequest, onCompleteCallback)

        //Assert
        assertEquals(walletBalanceTimelineRequest, actualWalletBalanceRequest)
    }

    @Test
    fun test_When_InvokingUseCaseWithSuccess_Expect_SuccessResultWithWalletTimelineToBeReceivedOnComplete() {
        //Arrange
        val walletBalanceService = stubWalletBalanceService(walletBalanceTimeline = expectedWalletBalanceTimeline)
        val sut = GetWalletBalanceTimelineUseCase(walletBalanceService)

        //Act
        sut.invoke(walletBalanceTimelineRequest, onCompleteCallback)

        //Assert
        assertTrue(actualResult is Success)
        assertEquals(expectedWalletBalanceTimeline, (actualResult as Success).payload)
    }

    @Test
    fun test_When_InvokingUseCaseWithFailure_Expect_FailureResultToBeReceivedOnComplete() {
        //Arrange
        val expectedErrorMessage = "Unable to get linked playlists"
        val walletBalanceService = stubWalletBalanceService(errorMessage = expectedErrorMessage)
        val sut = GetWalletBalanceTimelineUseCase(walletBalanceService)

        //Act
        sut.invoke(walletBalanceTimelineRequest, onCompleteCallback)

        //Assert
        assertTrue(actualResult is Failure)
        assertEquals(expectedErrorMessage, (actualResult as Failure).error.message)
    }

    private fun stubWalletBalanceService(walletBalanceTimeline: WalletBalanceTimeline? = null, errorMessage: String? = null): WalletBalanceService {

        return object: WalletBalanceService {
            override fun getAsOfWalletBalance(
                walletBalanceRequest: WalletBalanceRequest,
                onGetWalletBalance: (Result<WalletBalanceResult>) -> Unit,
            ) {}

            override fun getWalletBalanceTimeline(
                walletBalanceTimelineRequest: WalletBalanceTimelineRequest,
                onGetWalletBalanceTimeline: (Result<WalletBalanceTimeline>) -> Unit,
            ) {
                actualWalletBalanceRequest = walletBalanceTimelineRequest
                if(walletBalanceTimeline != null) {
                    return onGetWalletBalanceTimeline.invoke(Success(walletBalanceTimeline))
                }

                if(errorMessage != null) {
                    val libraryError = CuratorWalletException(errorMessage)
                    onGetWalletBalanceTimeline(Failure(libraryError))
                }
            }
        }
    }

}