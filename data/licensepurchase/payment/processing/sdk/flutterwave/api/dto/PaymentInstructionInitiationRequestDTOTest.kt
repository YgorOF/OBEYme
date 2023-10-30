package com.ygorxkharo.obey.web.data.licensepurchase.payment.processing.sdk.flutterwave.api.dto

import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class PaymentInstructionInitiationRequestDTOTest {

    @Test
    fun test_When_PaymentInstructionInitiationRequestIsConvertedToString_Expect_StringOutPutToMatchProperties() {
        //Arrange
        val expectedRequestBodyJson = PaymentInstructionInitiationTestFixtures.paymentRequestJsonString
        val requestBodyDTO = PaymentInstructionInitiationRequestDTO(
            transactionReference = "tx_2",
            amount = 20.0,
            currency = "ZAR",
            country = "ZA",
            redirectUri = "https://webhook.site/9d0b00ba-9a69-44fa-a43d-a82c33c36fdc",
            paymentOptions = " ",
            paymentPlanId = "13458",
            customerDetails = CustomerDetailsDTO(
                name = "Mark Bolo",
                phoneNumber = "0905837465",
                email = "markbolo20@gmail.com"
            ),
            paymentFormStyling = PaymentFormStylingDTO(
                title = "Pied Piper Payments",
                description = "Middleout isn't free. Pay the price",
                logoUrl = "https://assets.piedpiper.com/logo.png"
            )
        )

        //Act
        val actualRequestBodyJson = Json.encodeToString(PaymentInstructionInitiationRequestDTO.serializer(), requestBodyDTO)

        //Assert
        assertEquals(expectedRequestBodyJson, actualRequestBodyJson)
    }

}