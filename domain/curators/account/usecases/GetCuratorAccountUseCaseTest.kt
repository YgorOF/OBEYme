package com.ygorxkharo.obey.web.domain.curators.account.usecases

import com.ygorxkharo.obey.web.domain.common.Failure
import com.ygorxkharo.obey.web.domain.common.Result
import com.ygorxkharo.obey.web.domain.common.Success
import com.ygorxkharo.obey.web.domain.curators.account.CuratorAccountException
import com.ygorxkharo.obey.web.domain.curators.account.CuratorAccountService
import com.ygorxkharo.obey.web.domain.curators.account.model.CuratorAccountRequest
import com.ygorxkharo.obey.web.domain.curators.account.model.CuratorAccountResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetCuratorAccountUseCaseTest {

    private lateinit var actualCuratorAccountRequest: CuratorAccountRequest
    private lateinit var actualResult: Result<CuratorAccountResult>
    private val onCompleteCallback: (Result<CuratorAccountResult>) -> Unit = {
        actualResult = it
    }

    private val userId = "test_user_id_1"
    private val curatorAccountRequest = CuratorAccountRequest(userId = userId)

    @Test
    fun test_When_InvokingUseCase_Expect_CuratorAccountRequestToBePassedToCuratorAccountService() {
        //Arrange
        val curatorAccountService = stubCuratorAccountService()
        val sut = GetCuratorAccountUseCase(curatorAccountService)

        //Act
        sut.invoke(curatorAccountRequest, onCompleteCallback)

        //Assert
        assertEquals(curatorAccountRequest, actualCuratorAccountRequest)
    }

    @Test
    fun test_When_InvokingUseCaseWithSuccess_Expect_SuccessResultToBeReceivedOnComplete() {
        //Arrange
        val expectedCuratorAvailabilityValue = true
        val curatorAccountService = stubCuratorAccountService(hasCuratorLicense = expectedCuratorAvailabilityValue)
        val sut = GetCuratorAccountUseCase(curatorAccountService)

        //Act
        sut.invoke(curatorAccountRequest, onCompleteCallback)

        //Assert
        assertTrue(actualResult is Success)
        assertEquals(expectedCuratorAvailabilityValue, (actualResult as Success).payload.hasCuratorLicense)
    }

    @Test
    fun test_When_InvokingUseCaseWithFailure_Expect_FailureResultToBeReceivedOnComplete() {
        //Arrange
        val expectedErrorMessage = "Unable to get linked playlists"
        val curatorAccountService = stubCuratorAccountService(errorMessage = expectedErrorMessage)
        val sut = GetCuratorAccountUseCase(curatorAccountService)

        //Act
        sut.invoke(curatorAccountRequest, onCompleteCallback)

        //Assert
        assertTrue(actualResult is Failure)
        assertEquals(expectedErrorMessage, (actualResult as Failure).error.message)
    }

    private fun stubCuratorAccountService(hasCuratorLicense: Boolean? = null, errorMessage: String? = null): CuratorAccountService {

        return object: CuratorAccountService {
            override fun hasCuratorLicense(
                curatorAccountRequest: CuratorAccountRequest,
                onComplete: (Result<CuratorAccountResult>) -> Unit,
            ) {
                actualCuratorAccountRequest = curatorAccountRequest
                if(hasCuratorLicense != null) {
                    val curatorAccountResult = CuratorAccountResult(hasCuratorLicense = hasCuratorLicense)
                    return onComplete.invoke(Success(curatorAccountResult))
                }

                if(errorMessage != null) {
                    val userAccountError = CuratorAccountException(errorMessage)
                    onComplete(Failure(userAccountError))
                }
            }
        }
    }

}