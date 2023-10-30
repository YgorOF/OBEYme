package com.ygorxkharo.obey.web.data.licensepurchase.payment.processing.sdk.uuid

import kotlin.test.Test
import kotlin.test.assertTrue

class UUIDPaymentTransactionReferenceServiceTest {

    @Test
    fun test_When_GeneratingTransactionReference_Expect_AValidUUIDToBeReturned() {
        //Arrange
        val uuidRegexPattern = "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}\$".toRegex()
        val sut = UUIDPaymentTransactionReferenceService()

        //Act
        val actualTransactionReference = sut.generateReferenceID()

        //Assert
        assertTrue(uuidRegexPattern.matches(actualTransactionReference))
    }


}