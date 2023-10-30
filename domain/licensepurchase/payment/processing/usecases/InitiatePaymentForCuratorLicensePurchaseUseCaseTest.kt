package com.ygorxkharo.obey.web.domain.licensepurchase.payment.processing.usecases

import com.ygorxkharo.obey.web.domain.licensepurchase.payment.processing.model.PaymentInstructionRequest
import com.ygorxkharo.obey.web.domain.licensepurchase.payment.processing.PaymentInstructionService
import com.ygorxkharo.obey.web.domain.licensepurchase.payment.processing.model.InitiatedPaymentInstruction
import com.ygorxkharo.obey.web.domain.licensepurchase.payment.processing.model.PaymentInstruction
import kotlin.test.Test
import kotlin.test.assertEquals

class InitiatePaymentForCuratorLicensePurchaseUseCaseTest {

    private lateinit var actualPaymentInitiationRequest: PaymentInstructionRequest
    private val expectedPaymentInitiationRequest = PaymentInstructionRequest(
        userId = "test_user_1",
        amount = 5.0,
        localCurrencySymbol = "R",
        curatorLicenseId = "curator_license_id"
    )

    private lateinit var actualPaymentInstruction: PaymentInstruction
    private val onPurchaseInitiated: (InitiatedPaymentInstruction) -> Unit = {
        actualPaymentInstruction = it
    }

    @Test
    fun test_When_curatorLicensePurchaseInitiationSucceeds_Expect_SuccessFulInitiatedPaymentInstructionIsReceived() {
        //Arrange
        val expectedInitiatedPaymentInstruction = InitiatedPaymentInstruction(
            paymentFormUrl = "http://example.com",
            errorMessage = null
        )

        val paymentInstructionService = stubPaymentInstructionService(expectedInitiatedPaymentInstruction)
        val sut = InitiatePaymentForCuratorLicensePurchaseUseCase(paymentInstructionService)

        //Act
        sut.invoke(expectedPaymentInitiationRequest, onPurchaseInitiated)

        //Assert
        assertEquals(expectedPaymentInitiationRequest, actualPaymentInitiationRequest)
        assertEquals(expectedInitiatedPaymentInstruction, actualPaymentInstruction)
    }

    @Test
    fun test_When_curatorLicensePurchaseInitiationFails_Expect_FailedInitiatedPaymentInstructionIsReceived() {
        //Arrange
        val expectedInitiatedPaymentInstruction = InitiatedPaymentInstruction(
            paymentFormUrl = null,
            errorMessage = "Error initiating purchase request"
        )
        val paymentInstructionService = stubPaymentInstructionService(expectedInitiatedPaymentInstruction)
        val sut = InitiatePaymentForCuratorLicensePurchaseUseCase(paymentInstructionService)

        //Act
        sut.invoke(expectedPaymentInitiationRequest, onPurchaseInitiated)

        //Assert
        assertEquals(expectedPaymentInitiationRequest, actualPaymentInitiationRequest)
        assertEquals(expectedInitiatedPaymentInstruction, actualPaymentInstruction)
    }

    private fun stubPaymentInstructionService(initiatedPaymentInstruction: InitiatedPaymentInstruction): PaymentInstructionService {
        return object: PaymentInstructionService {
            override fun initiatePaymentInstruction(
                paymentInitiationRequest: PaymentInstructionRequest,
                onComplete: (InitiatedPaymentInstruction) -> Unit
            ) {
                actualPaymentInitiationRequest = paymentInitiationRequest
                onComplete.invoke(initiatedPaymentInstruction)
            }
        }
    }

}