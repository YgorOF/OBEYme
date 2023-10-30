package com.ygorxkharo.obey.web.data.licensepurchase.payment.processing.sdk.flutterwave.api.dto

import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class PaymentInstructionInitiationResponseDTOTest {

    @Test
    fun test_When_PaymentInstructionInitiationResponseIsConvertedToString_Expect_StringOutPutToMatchProperties() {
        //Arrange
        val expectedRequestBodyJson = PaymentInstructionInitiationTestFixtures.paymentResponseJsonString
        val responseBodyDTO = PaymentInstructionInitiationResponseDTO(
            status = "success",
            message = "Hosted Link",
            hostedLinkData = PaymentCaptureFormDTO(
                link = "https://ravemodal-dev.herokuapp.com/v3/hosted/pay/a04a10d191246aab0101"
            )
        )

        //Act
        val actualRequestBodyJson = Json.encodeToString(PaymentInstructionInitiationResponseDTO.serializer(), responseBodyDTO)

        //Assert
        assertEquals(expectedRequestBodyJson, actualRequestBodyJson)
    }

}