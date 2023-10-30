package com.ygorxkharo.obey.web.app.presentation.scenes.licensepurchase.viewmodel

import com.ygorxkharo.obey.web.app.presentation.common.statemanagement.model.UIOperationFailureStatus
import com.ygorxkharo.obey.web.app.presentation.common.statemanagement.model.UIOperationStatus
import com.ygorxkharo.obey.web.app.presentation.scenes.licensepurchase.model.CuratorLicenseCreditRetrievalStatus
import com.ygorxkharo.obey.web.app.presentation.scenes.licensepurchase.model.CuratorLicenseRetrievalSuccessStatus
import com.ygorxkharo.obey.web.data.platforms.MusicPlatform
import com.ygorxkharo.obey.web.domain.common.Failure
import com.ygorxkharo.obey.web.domain.common.Result
import com.ygorxkharo.obey.web.domain.common.Success
import com.ygorxkharo.obey.web.domain.creditfacility.CreditFacilityService
import com.ygorxkharo.obey.web.domain.creditfacility.activation.model.CuratorLicenseCredit
import com.ygorxkharo.obey.web.domain.creditfacility.activation.model.CuratorLicenseCreditRequest
import com.ygorxkharo.obey.web.domain.creditfacility.activation.model.CuratorLicenseCreditResult
import com.ygorxkharo.obey.web.domain.creditfacility.activation.usecases.GetCuratorLicenseCreditUseCase
import com.ygorxkharo.obey.web.domain.licensepurchase.CuratorLicenseService
import com.ygorxkharo.obey.web.domain.licensepurchase.model.CuratorLicense
import com.ygorxkharo.obey.web.domain.licensepurchase.model.CuratorLicenseType
import com.ygorxkharo.obey.web.domain.licensepurchase.usecases.GetCuratorLicensesUseCase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DefaultLicensePurchaseViewModelTest {

    private val musicPlatform = MusicPlatform.APPLE_MUSIC
    private val curatorLicense = CuratorLicense(
        id = "license_id_1",
        name = "Local curator license",
        subscriptionCostPerMonth = 9.99,
        backgroundColorValue = "#F5F5F5",
        creditCardBackgroundColor = "#5C96A8",
        headerColorValue = "",
        plaqueColorValue = "",
        benefits = listOf()
    )

    private lateinit var curatorLicenseRetrievalStatus: UIOperationStatus
    private val curatorLicenseCollectionObserver: (UIOperationStatus) -> Unit = {
        curatorLicenseRetrievalStatus = it
    }

    private lateinit var curatorLicenseCreditRetrievalStatus: UIOperationStatus
    private val creditBalanceUpdateObserver: (UIOperationStatus) -> Unit = {
        curatorLicenseCreditRetrievalStatus = it
    }

    private lateinit var actualLicenseName: String
    private lateinit var actualLicenseSubscription: Number
    private val purchaseBreakdownUpdateObserver: (String, Number) -> Unit = { licenseName, subscription ->
        actualLicenseName = licenseName
        actualLicenseSubscription = subscription
    }

    @Test
    fun test_When_LicensesAreAcquiredSuccessfully_Expect_AListOfLicenseAttributesToBePassedToTheUI() {
        //Arrange
        val getCuratorLicensesUseCase = stubGetLicensesUseCase(curatorLicense = curatorLicense, errorMessage = null)
        val getCuratorLicenseCreditUseCase = stubGetLicensesCreditUseCase(curatorLicenseCreditResult = null, errorMessage = null)
        val sut = DefaultLicensePurchaseViewModel(getCuratorLicensesUseCase, getCuratorLicenseCreditUseCase)
        sut.curatorLicenseCollectionObserver = curatorLicenseCollectionObserver

        //Act
        sut.getCuratorLicenseOffers(musicPlatform)

        //Assert
        assertTrue(curatorLicenseRetrievalStatus is CuratorLicenseRetrievalSuccessStatus)
        val uiOperationStatus = curatorLicenseRetrievalStatus as CuratorLicenseRetrievalSuccessStatus
        val curatorLicenseAttributes = uiOperationStatus.payload.first()

        assertEquals(curatorLicense.name, curatorLicenseAttributes.licenseName)
        assertEquals(curatorLicense.backgroundColorValue, curatorLicenseAttributes.backgroundColorValue)
        assertEquals(curatorLicense.creditCardBackgroundColor, curatorLicenseAttributes.creditCardProperties?.backgroundColorValue)
        assertEquals(curatorLicense.subscriptionCostPerMonth, curatorLicenseAttributes.subscriptionCostPerMonth)
        assertEquals(musicPlatform, curatorLicenseAttributes.creditCardProperties?.musicPlatform)
    }

    @Test
    fun test_When_ErrorOccursWhenFetchingLicenses_Expect_TheUIToReceiveAnErrorMessage() {
        //Arrange
        val expectedErrorMessage = "Error getting curator licenses"
        val getCuratorLicensesUseCase = stubGetLicensesUseCase(curatorLicense = null, errorMessage = expectedErrorMessage)
        val getCuratorLicenseCreditUseCase = stubGetLicensesCreditUseCase(curatorLicenseCreditResult = null, errorMessage = null)
        val sut = DefaultLicensePurchaseViewModel(getCuratorLicensesUseCase, getCuratorLicenseCreditUseCase)
        sut.curatorLicenseCollectionObserver = curatorLicenseCollectionObserver

        //Act
        sut.getCuratorLicenseOffers(musicPlatform)

        //Assert
        assertTrue(curatorLicenseRetrievalStatus is UIOperationFailureStatus)
        val uiOperationStatus = curatorLicenseRetrievalStatus as UIOperationFailureStatus

        assertEquals(expectedErrorMessage, uiOperationStatus.errorMessage)
    }

    @Test
    fun test_When_UpdatingPurchaseBreakdown_Expect_UIToUpdateLicenseNameAndSubscription() {
        //Arrange
        val expectedLicenseName = "Local curator license"
        val expectedLicenseSubscriptionValue = 9.99
        val getCuratorLicensesUseCase = stubGetLicensesUseCase(curatorLicense = curatorLicense, errorMessage = null)
        val getCuratorLicenseCreditUseCase = stubGetLicensesCreditUseCase(curatorLicenseCreditResult = null, errorMessage = null)
        val sut = DefaultLicensePurchaseViewModel(getCuratorLicensesUseCase, getCuratorLicenseCreditUseCase)
        sut.purchaseBreakdownUpdateObserver = purchaseBreakdownUpdateObserver
        sut.curatorLicenseCollectionObserver = curatorLicenseCollectionObserver
        sut.getCuratorLicenseOffers(musicPlatform)

        //Act
        sut.updateWithLicenseDetailsAtIndex(0)

        //Assert
        assertEquals(expectedLicenseName, actualLicenseName)
        assertEquals(expectedLicenseSubscriptionValue, actualLicenseSubscription)
    }

    @Test
    fun test_When_CreditBalanceForLicenseIsRetrievedSuccessfully_Expect_UIToUpdateWithCreditAmount() {
        //Arrange
        val expectedCuratorLicenseResult = CuratorLicenseCreditResult(
            curatorLicenseCredits = listOf(
                CuratorLicenseCredit(
                    curatorLicenseType = CuratorLicenseType.GLOBAL,
                    creditAmount = 5.10
                )
            )
        )
        val getCuratorLicensesUseCase = stubGetLicensesUseCase(curatorLicense = curatorLicense, errorMessage = null)
        val getCuratorLicenseCreditUseCase = stubGetLicensesCreditUseCase(
            curatorLicenseCreditResult = expectedCuratorLicenseResult,
            errorMessage = null
        )
        val sut = DefaultLicensePurchaseViewModel(getCuratorLicensesUseCase, getCuratorLicenseCreditUseCase)
        sut.creditBalanceUpdateObserver = creditBalanceUpdateObserver
        sut.curatorLicenseCollectionObserver = curatorLicenseCollectionObserver
        sut.getCuratorLicenseOffers(musicPlatform)

        //Act
        sut.getCreditBalanceForCuratorLicense(0)

        //Assert
        assertTrue(curatorLicenseCreditRetrievalStatus is CuratorLicenseCreditRetrievalStatus)
        val uiOperationStatus = curatorLicenseCreditRetrievalStatus as CuratorLicenseCreditRetrievalStatus
        assertEquals(expectedCuratorLicenseResult.curatorLicenseCredits, uiOperationStatus.payload)
    }

    @Test
    fun test_When_AnErrorOccursWhileGettingCreditBalance_Expect_UIToUpdateWithAnErrorMessage() {
        //Arrange
        val expectedErrorMessage = "Error fetching credit balance for this curator license"
        val getCuratorLicensesUseCase = stubGetLicensesUseCase(curatorLicense = curatorLicense, errorMessage = null)
        val getCuratorLicenseCreditUseCase = stubGetLicensesCreditUseCase(
            curatorLicenseCreditResult = null,
            errorMessage = expectedErrorMessage
        )
        val sut = DefaultLicensePurchaseViewModel(getCuratorLicensesUseCase, getCuratorLicenseCreditUseCase)
        sut.creditBalanceUpdateObserver = creditBalanceUpdateObserver
        sut.curatorLicenseCollectionObserver = curatorLicenseCollectionObserver
        sut.getCuratorLicenseOffers(musicPlatform)

        //Act
        sut.getCreditBalanceForCuratorLicense(0)

        //Assert
        assertTrue(curatorLicenseCreditRetrievalStatus is UIOperationFailureStatus)
        val uiOperationStatus = curatorLicenseCreditRetrievalStatus as UIOperationFailureStatus
        assertEquals(expectedErrorMessage, uiOperationStatus.errorMessage)
    }

    private fun stubGetLicensesUseCase(curatorLicense: CuratorLicense?, errorMessage: String?): GetCuratorLicensesUseCase {
        val curatorLicenseService = object: CuratorLicenseService {
            override fun getCuratorLicensesAsync(onComplete: (Result<List<CuratorLicense>>) -> Unit) {
                if(curatorLicense != null) {
                    onComplete.invoke(Success(listOf(curatorLicense)))
                }

                if(errorMessage != null) {
                    onComplete.invoke(Failure(Exception(errorMessage)))
                }
            }
        }

        return GetCuratorLicensesUseCase(curatorLicenseService)
    }

    private fun stubGetLicensesCreditUseCase(curatorLicenseCreditResult: CuratorLicenseCreditResult?, errorMessage: String?): GetCuratorLicenseCreditUseCase {
        val creditFacilityService = object: CreditFacilityService {
            override fun getCreditByLicenseTypeAsync(
                curatorLicenseCreditRequest: CuratorLicenseCreditRequest,
                onComplete: (Result<CuratorLicenseCreditResult>) -> Unit
            ) {
                if(curatorLicenseCreditResult != null) {
                    onComplete.invoke(Success(curatorLicenseCreditResult))
                }

                if(errorMessage != null) {
                    onComplete.invoke(Failure(Exception(errorMessage)))
                }
            }
        }

        return GetCuratorLicenseCreditUseCase(creditFacilityService)
    }

}