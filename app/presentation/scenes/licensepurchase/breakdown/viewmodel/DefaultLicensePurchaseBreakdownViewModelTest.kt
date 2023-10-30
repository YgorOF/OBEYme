package com.ygorxkharo.obey.web.app.presentation.scenes.licensepurchase.breakdown.viewmodel

import com.ygorxkharo.obey.web.app.presentation.common.statemanagement.model.UIOperationFailureStatus
import com.ygorxkharo.obey.web.app.presentation.common.statemanagement.model.UIOperationStatus
import com.ygorxkharo.obey.web.app.presentation.scenes.licensepurchase.breakdown.model.PaymentInitiationSuccessStatus
import com.ygorxkharo.obey.web.domain.licensepurchase.payment.processing.PaymentInstructionService
import com.ygorxkharo.obey.web.domain.licensepurchase.payment.processing.model.InitiatedPaymentInstruction
import com.ygorxkharo.obey.web.domain.licensepurchase.payment.processing.model.PaymentInstructionRequest
import com.ygorxkharo.obey.web.domain.licensepurchase.payment.processing.usecases.InitiatePaymentForCuratorLicensePurchaseUseCase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DefaultLicensePurchaseBreakdownViewModelTest {

    private lateinit var paymentInitiationStatus: UIOperationStatus
    private val paymentInitiationStatusObserver: (UIOperationStatus) -> Unit = {
        paymentInitiationStatus = it
    }
    private val userId = "test_user_id"
    private val curatorLicenseId = "license_id"
    private val licenseAmount = 45.0
    private val localCurrencySymbol = "R"

    @Test
    fun test_when_PaymentInitiationIsSuccessful_Expect_UIStatusToBeTriggeredWithPaymentInitiationSuccessStatus() {
        //Arrange
        val paymentFormLink = "https://rave.taxify.payment.com"
        val initiateCuratorLicensePurchaseUseCase = stubInitiateCuratorLicensePurchaseUseCase(paymentFormLink = paymentFormLink)
        val sut = DefaultLicensePurchaseBreakdownViewModel(initiateCuratorLicensePurchaseUseCase)
        sut.paymentInitiationStatusObserver = paymentInitiationStatusObserver

        //Act
        sut.buyCuratorLicense(
            userId = userId,
            curatorLicenseId = curatorLicenseId,
            amount = licenseAmount,
            localCurrencySymbol = localCurrencySymbol
        )

        //Assert
        assertTrue(paymentInitiationStatus is PaymentInitiationSuccessStatus)
        val successStatus = paymentInitiationStatus as PaymentInitiationSuccessStatus
        assertEquals(paymentFormLink, successStatus.payload)
    }

    @Test
    fun test_when_PaymentInitiationFails_Expect_UIStatusToBeTriggeredWithUIOperationFailureStatus() {
        //Arrange
        val errorMessage = "Error initiating curator license purchase"
        val initiateCuratorLicensePurchaseUseCase = stubInitiateCuratorLicensePurchaseUseCase(errorMessage = errorMessage)
        val sut = DefaultLicensePurchaseBreakdownViewModel(initiateCuratorLicensePurchaseUseCase)
        sut.paymentInitiationStatusObserver = paymentInitiationStatusObserver

        //Act
        sut.buyCuratorLicense(
            userId = userId,
            curatorLicenseId = curatorLicenseId,
            amount = licenseAmount,
            localCurrencySymbol = localCurrencySymbol
        )

        //Assert
        assertTrue(paymentInitiationStatus is UIOperationFailureStatus)
        val failureStatus = paymentInitiationStatus as UIOperationFailureStatus
        assertEquals(errorMessage, failureStatus.errorMessage)
    }

    @Test
    fun test_when_PaymentInitiationFailsDueToInvalidPaymentFormURL_Expect_UIStatusToBeTriggeredWithUIOperationFailureStatus() {
        //Arrange
        val paymentFormLink = "aaaaaaaaa"
        val expectedErrorMessage = "Invalid payment form URL received."
        val initiateCuratorLicensePurchaseUseCase = stubInitiateCuratorLicensePurchaseUseCase(paymentFormLink = paymentFormLink)
        val sut = DefaultLicensePurchaseBreakdownViewModel(initiateCuratorLicensePurchaseUseCase)
        sut.paymentInitiationStatusObserver = paymentInitiationStatusObserver

        //Act
        sut.buyCuratorLicense(
            userId = userId,
            curatorLicenseId = curatorLicenseId,
            amount = licenseAmount,
            localCurrencySymbol = localCurrencySymbol
        )

        //Assert
        assertTrue(paymentInitiationStatus is UIOperationFailureStatus)
        val failureStatus = paymentInitiationStatus as UIOperationFailureStatus
        assertEquals(expectedErrorMessage, failureStatus.errorMessage)
    }

    private fun stubInitiateCuratorLicensePurchaseUseCase(
        paymentFormLink: String? = null,
        errorMessage: String? = null
    ): InitiatePaymentForCuratorLicensePurchaseUseCase {
        val paymentInstructionService = object: PaymentInstructionService {
            override fun initiatePaymentInstruction(
                paymentInitiationRequest: PaymentInstructionRequest,
                onComplete: (InitiatedPaymentInstruction) -> Unit
            ) {
                if(paymentFormLink != null) {
                    return onComplete(InitiatedPaymentInstruction(paymentFormUrl = paymentFormLink))
                }

                if(errorMessage != null) {
                    onComplete(InitiatedPaymentInstruction(errorMessage = errorMessage))
                }
            }
        }
        return InitiatePaymentForCuratorLicensePurchaseUseCase(paymentInstructionService)
    }

}