package com.ygorxkharo.obey.web.data.licensepurchase.payment.processing

import com.ygorxkharo.obey.web.domain.licensepurchase.payment.processing.PaymentProcessor
import com.ygorxkharo.obey.web.domain.licensepurchase.payment.processing.model.InitiatedPaymentInstruction
import com.ygorxkharo.obey.web.domain.licensepurchase.payment.processing.model.PaymentInstructionRequest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DefaultPaymentInstructionServiceTest {

    private val paymentInstructionRequest = PaymentInstructionRequest(
        userId = "",
        localCurrencySymbol = "R",
        amount = 5.0,
        curatorLicenseId = ""
    )

    private lateinit var actualInitiatedPaymentInstruction: InitiatedPaymentInstruction
    private val onCompleteCallback: (InitiatedPaymentInstruction) -> Unit = {
        actualInitiatedPaymentInstruction = it
    }
    private val paymentFormLink = "http://copy-copy.com"
    private val errorMessage = "Error initiating payment"

    @Test
    fun test_When_InitiatedPaymentInstructionSuccessfully_Expect_InitiatedInstructionToContainPaymentLink() {
        //Arrange
        val expectedInitiatedPaymentInstruction = InitiatedPaymentInstruction(paymentFormUrl = paymentFormLink)
        val paymentProcessorLocation = "ZA"
        val sut = buildSUT(expectedInitiatedPaymentInstruction, paymentProcessorLocation)

        //Act
        sut.initiatePaymentInstruction(paymentInstructionRequest, onCompleteCallback)

        //Assert
        assertEquals(paymentFormLink, actualInitiatedPaymentInstruction.paymentFormUrl)
        assertEquals(null, actualInitiatedPaymentInstruction.errorMessage)
    }

    @Test
    fun test_When_InitiatedPaymentInstructionFails_Expect_InitiatedInstructionToContainErrorMessage() {
        //Arrange
        val expectedInitiatedPaymentInstruction = InitiatedPaymentInstruction(errorMessage = errorMessage)
        val paymentProcessorLocation = "ZA"
        val sut = buildSUT(expectedInitiatedPaymentInstruction, paymentProcessorLocation)

        //Act
        sut.initiatePaymentInstruction(paymentInstructionRequest, onCompleteCallback)

        //Assert
        assertEquals(errorMessage, actualInitiatedPaymentInstruction.errorMessage)
        assertEquals(null, actualInitiatedPaymentInstruction.paymentFormUrl)
    }

    @Test
    fun test_When_PaymentProcessorIsUnavailableForDefaultLocation_Expect_ExceptionToBeThrown() {
        //Arrange
        val expectedExceptionMessage = "Payment processor unavailable for this location"
        val expectedInitiatedPaymentInstruction = InitiatedPaymentInstruction()
        val paymentProcessorLocation = "NG"
        val sut = buildSUT(expectedInitiatedPaymentInstruction, paymentProcessorLocation)

        //Act
        val actualException = try {
            sut.initiatePaymentInstruction(paymentInstructionRequest, onCompleteCallback)
            null
        } catch (ex: Exception) {
            ex
        }

        //Assert
        assertTrue(actualException is PaymentProcessorException)
        assertEquals(expectedExceptionMessage, actualException.message)
    }

    private fun buildSUT(
        initiatedPaymentInstruction: InitiatedPaymentInstruction,
        paymentProcessorLocation: String
    ): DefaultPaymentInstructionService {
        val paymentProcessor = stubPaymentProcessor(initiatedPaymentInstruction)
        val paymentProcessors = mapOf(paymentProcessorLocation to paymentProcessor)
        return DefaultPaymentInstructionService(paymentProcessors)
    }

    private fun stubPaymentProcessor(initiatedPaymentInstruction: InitiatedPaymentInstruction): PaymentProcessor {
        return object: PaymentProcessor {
            override fun onInitiate(
                paymentInstructionRequest: PaymentInstructionRequest,
                onInitiationCompleted: (InitiatedPaymentInstruction) -> Unit
            ) {
                onInitiationCompleted.invoke(initiatedPaymentInstruction)
            }
        }
    }

}