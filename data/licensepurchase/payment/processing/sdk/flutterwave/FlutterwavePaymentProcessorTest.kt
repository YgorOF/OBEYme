package com.ygorxkharo.obey.web.data.licensepurchase.payment.processing.sdk.flutterwave

import com.ygorxkharo.obey.web.data.licensepurchase.payment.processing.PaymentInitiationRequestBuilder
import com.ygorxkharo.obey.web.data.licensepurchase.payment.processing.PaymentProcessorAPI
import com.ygorxkharo.obey.web.data.licensepurchase.payment.processing.PaymentTransactionReferenceService
import com.ygorxkharo.obey.web.data.licensepurchase.payment.processing.sdk.flutterwave.api.dto.CustomerDetailsDTO
import com.ygorxkharo.obey.web.data.licensepurchase.payment.processing.sdk.flutterwave.api.dto.PaymentTransactionBody
import com.ygorxkharo.obey.web.domain.common.Failure
import com.ygorxkharo.obey.web.domain.common.Result
import com.ygorxkharo.obey.web.domain.common.Success
import com.ygorxkharo.obey.web.domain.licensepurchase.payment.processing.model.InitiatedPaymentInstruction
import com.ygorxkharo.obey.web.domain.licensepurchase.payment.processing.model.PaymentInstructionRequest
import com.ygorxkharo.obey.web.domain.sessionmanagement.musicplatforms.MusicPlatformAccountService
import com.ygorxkharo.obey.web.domain.sessionmanagement.musicplatforms.model.MusicPlatformAccountSession
import com.ygorxkharo.obey.web.domain.sessionmanagement.musicplatforms.model.MusicPlatformAccountSessionRequest
import org.w3c.fetch.RequestInit
import kotlin.test.Test
import kotlin.test.assertEquals

class FlutterwavePaymentProcessorTest {

    private lateinit var actualPaymentInstructionInitiationResult: InitiatedPaymentInstruction
    private val onInitiationCompleted: (InitiatedPaymentInstruction) -> Unit = {
        actualPaymentInstructionInitiationResult = it
    }
    private val paymentInstructionRequest = PaymentInstructionRequest(
        userId = "test_user_id",
        curatorLicenseId = "curator_license_id",
        amount = 20.0,
        localCurrencySymbol = "R"
    )
    private val userAccountErrorMessage = "Error getting user details"
    private val transactionReferenceService = stubTransactionRefService()
    private val paymentRequestBuilder = stubRequestBuilder()

    @Test
    fun test_When_PaymentInstructionInitiationIsSuccessful_Expect_ThePaymentFormUrlToBeAvailable() {
        //Arrange
        val expectedPaymentFormUrl = "https://ravemodal-dev.herokuapp.com/v3/hosted/pay/a04a10d191246aab0101"
        val expectedErrorMessage = null
        val userAccountService = stubUserAccountService(isSuccessful = true)
        val paymentProcessorAPI = stubPaymentProcessorAPI(paymentFormURL = expectedPaymentFormUrl, errorMessage = null)
        val sut = buildSUT(paymentProcessorAPI, userAccountService)

        //Act
        sut.onInitiate(
            paymentInstructionRequest = paymentInstructionRequest,
            onInitiationCompleted = this.onInitiationCompleted
        )

        //Assert
        assertEquals(expectedPaymentFormUrl, actualPaymentInstructionInitiationResult.paymentFormUrl)
        assertEquals(expectedErrorMessage, actualPaymentInstructionInitiationResult.errorMessage)
    }


    @Test
    fun test_When_PaymentInstructionInitiationFails_Expect_ThePaymentInstructionErrorMessageToBeAvailable() {
        //Arrange
        val expectedPaymentFormUrl = null
        val expectedErrorMessage = "Error initiating payment instruction"
        val userAccountService = stubUserAccountService(isSuccessful = true)
        val paymentProcessorAPI = stubPaymentProcessorAPI(paymentFormURL = null, errorMessage = expectedErrorMessage)
        val sut = buildSUT(paymentProcessorAPI, userAccountService)

        //Act
        sut.onInitiate(
            paymentInstructionRequest = paymentInstructionRequest,
            onInitiationCompleted = this.onInitiationCompleted
        )

        //Assert
        assertEquals(expectedPaymentFormUrl, actualPaymentInstructionInitiationResult.paymentFormUrl)
        assertEquals(expectedErrorMessage, actualPaymentInstructionInitiationResult.errorMessage)
    }

    @Test
    fun test_When_PaymentInstructionInitiationFailsDueToUserAccountError_Expect_TheInitiationInstructionToContainUserAccountErrorMessage() {
        //Arrange
        val expectedPaymentFormUrl = null
        val expectedErrorMessage = userAccountErrorMessage
        val userAccountService = stubUserAccountService(isSuccessful = false)
        val paymentProcessorAPI = stubPaymentProcessorAPI(paymentFormURL = null, errorMessage = null)

        val sut = buildSUT(paymentProcessorAPI, userAccountService)

        //Act
        sut.onInitiate(
            paymentInstructionRequest = paymentInstructionRequest,
            onInitiationCompleted = this.onInitiationCompleted
        )

        //Assert
        assertEquals(expectedPaymentFormUrl, actualPaymentInstructionInitiationResult.paymentFormUrl)
        assertEquals(expectedErrorMessage, actualPaymentInstructionInitiationResult.errorMessage)
    }

    private fun buildSUT(
        paymentProcessorAPI: PaymentProcessorAPI<RequestInit>,
        userAccountService: MusicPlatformAccountService
    ): FlutterwavePaymentProcessor {
        return FlutterwavePaymentProcessor(
            paymentProcessorAPI = paymentProcessorAPI,
            paymentRequestBuilder = paymentRequestBuilder,
            transactionReferenceService = transactionReferenceService,
            userAccountService = userAccountService
        )
    }

    private fun stubPaymentProcessorAPI(paymentFormURL: String?, errorMessage: String?): PaymentProcessorAPI<RequestInit> {
        return object: PaymentProcessorAPI<RequestInit> {
            override fun initiatePaymentInstruction(
                paymentTransactionRequest: RequestInit,
                onInitiationCompleted: (InitiatedPaymentInstruction) -> Unit
            ) {
                val initiatedPaymentInstruction = InitiatedPaymentInstruction(paymentFormUrl = paymentFormURL, errorMessage = errorMessage)
                return onInitiationCompleted.invoke(initiatedPaymentInstruction)
            }
        }
    }

    private fun stubRequestBuilder(): PaymentInitiationRequestBuilder<PaymentTransactionBody, CustomerDetailsDTO, RequestInit> {
        return object: PaymentInitiationRequestBuilder<PaymentTransactionBody, CustomerDetailsDTO, RequestInit> {
            override fun buildRequest(
                paymentTransactionBody: PaymentTransactionBody,
                customerDetails: CustomerDetailsDTO,
                method: String
            ): RequestInit {
                return RequestInit(body = "", method = "")
            }
        }
    }

    private fun stubTransactionRefService(): PaymentTransactionReferenceService = object: PaymentTransactionReferenceService {
        override fun generateReferenceID() = "tx_123"
    }

    private fun stubUserAccountService(isSuccessful: Boolean): MusicPlatformAccountService = object: MusicPlatformAccountService {

        override fun getUserAccountAsync(
            musicPlatformAccountSessionRequest: MusicPlatformAccountSessionRequest,
            onGetUserAccountSession: (Result<MusicPlatformAccountSession>) -> Unit
        ) {
            if(isSuccessful) {
                val userSessionDetails = MusicPlatformAccountSession(
                    name = "Mark Bolo",
                    email = "markbolo@gmail.com",
                    phoneNumber = null,
                    username = ""
                )

                onGetUserAccountSession.invoke(Success(userSessionDetails))
            } else {
                onGetUserAccountSession.invoke(Failure(Exception(userAccountErrorMessage)))
            }
        }

    }

}