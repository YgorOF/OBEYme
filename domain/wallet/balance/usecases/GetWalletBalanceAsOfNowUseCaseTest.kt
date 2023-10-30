package com.ygorxkharo.obey.web.domain.wallet.balance.usecases

import com.ygorxkharo.obey.web.domain.common.Failure
import com.ygorxkharo.obey.web.domain.common.Result
import com.ygorxkharo.obey.web.domain.common.Success
import com.ygorxkharo.obey.web.domain.wallet.CuratorWalletException
import com.ygorxkharo.obey.web.domain.wallet.balance.WalletBalanceService
import com.ygorxkharo.obey.web.domain.wallet.balance.model.WalletBalanceRequest
import com.ygorxkharo.obey.web.domain.wallet.balance.model.WalletBalanceResult
import com.ygorxkharo.obey.web.domain.wallet.balance.timeline.model.WalletBalanceTimeline
import com.ygorxkharo.obey.web.domain.wallet.balance.timeline.model.WalletBalanceTimelineRequest
import kotlin.js.Date
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetWalletBalanceAsOfNowUseCaseTest {

    private lateinit var actualWalletBalanceRequest: WalletBalanceRequest
    private lateinit var actualResult: Result<WalletBalanceResult>
    private val onCompleteCallback: (Result<WalletBalanceResult>) -> Unit = {
        actualResult = it
    }
    private val walletBalanceRequestDate = Date()
    private val walletBalanceRequest = WalletBalanceRequest(userId = "test_user_2232", walletBalanceRequestDate)
    private val walletBusinessDate = Date("2021-06-21")
    private val walletCurrency = "ZAR"

    @Test
    fun test_When_InvokingUseCase_Expect_WalletBalanceRequestToBePassedToWalletBalanceService() {
        //Arrange
        val walletBalanceService = stubWalletBalanceService()
        val sut = GetWalletBalanceAsOfNowUseCase(walletBalanceService)

        //Act
        sut.invoke(walletBalanceRequest, onCompleteCallback)

        //Assert
        assertEquals(walletBalanceRequest, actualWalletBalanceRequest)
    }

    @Test
    fun test_When_InvokingUseCaseWithSuccess_Expect_SuccessResultToBeReceivedOnComplete() {
        //Arrange
        val expectedWalletBalance = 49.34
        val walletBalanceService = stubWalletBalanceService(walletBalanceAmount =  expectedWalletBalance)
        val sut = GetWalletBalanceAsOfNowUseCase(walletBalanceService)

        //Act
        sut.invoke(walletBalanceRequest, onCompleteCallback)

        //Assert
        assertTrue(actualResult is Success)
        assertEquals(expectedWalletBalance, (actualResult as Success).payload.amount)
    }

    @Test
    fun test_When_InvokingUseCaseWithFailure_Expect_FailureResultToBeReceivedOnComplete() {
        //Arrange
        val expectedErrorMessage = "Unable to get linked playlists"
        val walletBalanceService = stubWalletBalanceService(errorMessage = expectedErrorMessage)
        val sut = GetWalletBalanceAsOfNowUseCase(walletBalanceService)

        //Act
        sut.invoke(walletBalanceRequest, onCompleteCallback)

        //Assert
        assertTrue(actualResult is Failure)
        assertEquals(expectedErrorMessage, (actualResult as Failure).error.message)
    }

    private fun stubWalletBalanceService(walletBalanceAmount: Double? = null, errorMessage: String? = null): WalletBalanceService {

        return object: WalletBalanceService {
            override fun getAsOfWalletBalance(
                walletBalanceRequest: WalletBalanceRequest,
                onGetWalletBalance: (Result<WalletBalanceResult>) -> Unit,
            ) {
                actualWalletBalanceRequest = walletBalanceRequest
                if(walletBalanceAmount != null) {
                    val walletBalanceResult = WalletBalanceResult(
                        amount = walletBalanceAmount,
                        currency = walletCurrency,
                        businessDate = walletBusinessDate
                    )
                    return onGetWalletBalance.invoke(Success(walletBalanceResult))
                }

                if(errorMessage != null) {
                    val libraryError = CuratorWalletException(errorMessage)
                    onGetWalletBalance(Failure(libraryError))
                }
            }

            override fun getWalletBalanceTimeline(
                walletBalanceTimelineRequest: WalletBalanceTimelineRequest,
                onGetWalletBalanceTimeline: (Result<WalletBalanceTimeline>) -> Unit,
            ) {}
        }
    }

}