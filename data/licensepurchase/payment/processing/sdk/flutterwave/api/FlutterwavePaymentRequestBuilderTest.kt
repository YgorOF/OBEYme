package com.ygorxkharo.obey.web.data.licensepurchase.payment.processing.sdk.flutterwave.api

import com.ygorxkharo.obey.web.data.licensepurchase.payment.processing.CurrencySymbolToMonetaryUnitResolver
import com.ygorxkharo.obey.web.data.licensepurchase.payment.processing.sdk.flutterwave.api.dto.CustomerDetailsDTO
import com.ygorxkharo.obey.web.data.licensepurchase.payment.processing.sdk.flutterwave.api.dto.PaymentFormStylingDTO
import com.ygorxkharo.obey.web.data.licensepurchase.payment.processing.sdk.flutterwave.api.dto.PaymentInstructionInitiationTestFixtures
import com.ygorxkharo.obey.web.data.licensepurchase.payment.processing.sdk.flutterwave.api.dto.PaymentTransactionBody
import kotlin.test.Test
import kotlin.test.assertEquals

class FlutterwavePaymentRequestBuilderTest {

    private val paymentOptions = " "
    private val redirectUrl = "https://webhook.site/9d0b00ba-9a69-44fa-a43d-a82c33c36fdc"
    private val paymentInstructionInitiationBody = PaymentTransactionBody(
        transactionReference = "tx_2",
        transactionAmount = 20.0,
        localCurrencySymbol = "R",
        paymentPlanId = "13458"
    )
    private val userAccountDetails = CustomerDetailsDTO(
        name = "Mark Bolo",
        email = "markbolo20@gmail.com",
        phoneNumber = "0905837465"
    )
    private val paymentFormStyling = PaymentFormStylingDTO(
        title = "Pied Piper Payments",
        description = "Middleout isn't free. Pay the price",
        logoUrl = "https://assets.piedpiper.com/logo.png"
    )

    private val currencySymbolToMonetaryUnitResolver = object: CurrencySymbolToMonetaryUnitResolver {
        override fun getCurrencyCodeFromCurrencySymbol(localCurrencySymbol: String) = "ZAR"
        override fun getCountryCodeFromCurrencySymbol(localCurrencySymbol: String) = "ZA"
        override fun getCurrencySymbolFromCurrencyCode(currencyCode: String): String = "R"
    }

    private val sut = FlutterwavePaymentRequestBuilder(
        currencySymbolToMonetaryUnitResolver,
        paymentOptions = paymentOptions,
        redirectUrl = redirectUrl,
        paymentFormStyling = paymentFormStyling
    )

    @Test
    fun test_When_BuildingRequestBody_Expect_PaymentInstructionInitiationRequestDTOToBeReturned() {
        //Arrange
        val expectedRequestBody = PaymentInstructionInitiationTestFixtures.paymentRequestJsonString
        val expectedMethodName = "POST"


        //Act
        val actualRequest = sut.buildRequest(
            paymentTransactionBody = paymentInstructionInitiationBody,
            customerDetails = userAccountDetails,
            method = expectedMethodName
        )

        //Assert
        assertEquals(expectedRequestBody, actualRequest.body)
        assertEquals(expectedMethodName, actualRequest.method)
    }

}