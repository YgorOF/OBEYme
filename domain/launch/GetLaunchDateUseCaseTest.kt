package com.ygorxkharo.obey.web.domain.launch

import com.ygorxkharo.obey.web.domain.common.Failure
import com.ygorxkharo.obey.web.domain.common.Result
import com.ygorxkharo.obey.web.domain.common.Success
import kotlin.js.Date
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetLaunchDateUseCaseTest {

    private lateinit var actualLaunchDateResult: Result<Double>
    private val onCompleteCallback: (Result<Double>) -> Unit = {
        actualLaunchDateResult = it
    }

    @Test
    fun test_When_LaunchDateAcquiredSuccessfully_Expect_SuccessResultToBePassedToOnCompleteCallback() {
        //Arrange
        val appLaunchDate = Date("Sat Feb 15 2020 14:01:19 GMT+0300 (East Africa Time)")
        val expectedLaunchDateTimestamp = appLaunchDate.getTime()
        val launchCampaignConfigService = stubLaunchCampaignConfigService(launchDate = expectedLaunchDateTimestamp)
        val sut = GetLaunchDateUseCase(launchCampaignConfigService)

        //Act
        sut.getUTCTimestamp(onCompleteCallback)

        //Assert
        assertTrue(actualLaunchDateResult is Success)
        assertEquals(expectedLaunchDateTimestamp, (actualLaunchDateResult as Success).payload)
    }

    @Test
    fun test_When_ErrorOccursWhenGettingLaunchDate_Expect_FailureResultToBePassedToOnCompleteCallback() {
        //Arrange
        val expectedErrorMessage = "Unable to get launch date. Please try again in a bit"
        val launchCampaignConfigService = stubLaunchCampaignConfigService(errorMessage = expectedErrorMessage)
        val sut = GetLaunchDateUseCase(launchCampaignConfigService)

        //Act
        sut.getUTCTimestamp(onCompleteCallback)

        //Assert
        assertTrue(actualLaunchDateResult is Failure)
        assertEquals(expectedErrorMessage, (actualLaunchDateResult as Failure).error.message)
    }

    private fun stubLaunchCampaignConfigService(
        launchDate: Double? = null,
        errorMessage: String? = null
    ): LaunchCampaignConfigService {
        return object: LaunchCampaignConfigService {
            override fun getLaunchDateUTCTimestamp(onComplete: (Result<Double>) -> Unit) {
                if(launchDate != null) {
                    return onComplete.invoke(Success(launchDate))
                }

                if(errorMessage != null) {
                    return onComplete.invoke(Failure(IllegalStateException(errorMessage)))
                }
            }
        }
    }

}