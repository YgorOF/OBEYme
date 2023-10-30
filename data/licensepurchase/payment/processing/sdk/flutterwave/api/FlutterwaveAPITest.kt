package com.ygorxkharo.obey.web.data.licensepurchase.payment.processing.sdk.flutterwave.api

import com.ygorxkharo.obey.web.data.common.browser.http.HttpClient
import com.ygorxkharo.obey.web.data.licensepurchase.payment.processing.sdk.flutterwave.api.dto.PaymentInstructionInitiationTestFixtures
import com.ygorxkharo.obey.web.domain.common.Failure
import com.ygorxkharo.obey.web.domain.common.Result
import com.ygorxkharo.obey.web.domain.common.Success
import com.ygorxkharo.obey.web.domain.licensepurchase.payment.processing.model.InitiatedPaymentInstruction
import org.w3c.fetch.RequestInit
import kotlin.test.Test
import kotlin.test.assertEquals

class FlutterwaveAPITest {

    private val paymentEndpoint = "http://payment-endpoint.com"
    private val paymentTransactionRequest = RequestInit()
    private lateinit var initiatedPaymentInstruction: InitiatedPaymentInstruction
    private val onInitiationCompleted: (InitiatedPaymentInstruction) -> Unit = {
        initiatedPaymentInstruction = it
    }

    @Test
    fun test_When_PaymentInitiationRequestSucceeds_Expect_InitiatedInstructionToContainPaymentFormLink() {
        //Arrange
        val responsePayload = PaymentInstructionInitiationTestFixtures.paymentResponseJsonString
        val expectedErrorMessage = null
        val httpClient = stubHttpClient(responsePayload = responsePayload, errorMessage = expectedErrorMessage)
        val sut = FlutterwaveAPI(paymentEndpoint = paymentEndpoint, httpClient = httpClient)
        val expectedPaymentFormUrl = "https://ravemodal-dev.herokuapp.com/v3/hosted/pay/a04a10d191246aab0101"

        //Act
        sut.initiatePaymentInstruction(paymentTransactionRequest, onInitiationCompleted)

        //Assert
        assertEquals(expectedPaymentFormUrl, initiatedPaymentInstruction.paymentFormUrl)
        assertEquals(expectedErrorMessage, initiatedPaymentInstruction.errorMessage)
    }

    @Test
    fun test_When_PaymentInitiationRequestFails_Expect_InitiatedInstructionToContainErrorMessage() {
        //Arrange
        val responsePayload = null
        val expectedErrorMessage = "Error initiating payment"
        val httpClient = stubHttpClient(responsePayload = responsePayload, errorMessage = expectedErrorMessage)
        val sut = FlutterwaveAPI(paymentEndpoint = paymentEndpoint, httpClient = httpClient)
        val expectedPaymentFormUrl = null

        //Act
        sut.initiatePaymentInstruction(paymentTransactionRequest, onInitiationCompleted)

        //Assert
        assertEquals(expectedPaymentFormUrl, initiatedPaymentInstruction.paymentFormUrl)
        assertEquals(expectedErrorMessage, initiatedPaymentInstruction.errorMessage)
    }

    private fun stubHttpClient(responsePayload: String?, errorMessage: String?): HttpClient<RequestInit, String> {
        return object: HttpClient<RequestInit, String> {
            override fun handleRequest(
                destinationUrl: String,
                requestOptions: RequestInit,
                onRequestHandled: (Result<String>) -> Unit
            ) {
                if(responsePayload != null) {
                    return onRequestHandled(Success(responsePayload))
                }

                if(errorMessage != null) {
                    return onRequestHandled(Failure(IllegalStateException(errorMessage)))
                }
            }
        }
    }

}