package com.ygorxkharo.obey.web.data.licensepurchase.payment.validation

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DefaultPaymentCardDetailsValidatorTest {

    private val sut = LuhnPaymentCardNumberValidator()

    @Test
    fun test_When_ValidCardNumberIsUsed_Expect_CardNumberValidationToPass() {
        //Arrange
        val cardNumber = "5412 7534 5678 9010"

        //Act
        val actualValidationResponse = sut.validateCardNumber(cardNumber)

        //Assert
        assertTrue(actualValidationResponse)
    }

    @Test
    fun test_When_InvalidCardNumberIsUsed_Expect_CardNumberValidationToFail() {
        //Arrange
        val cardNumber = "5412 7534 5678 9011"

        //Act
        val actualValidationResponse = sut.validateCardNumber(cardNumber)

        //Assert
        assertFalse(actualValidationResponse)
    }

    @Test
    fun test_When_EmptyCardNumberIsUsed_Expect_CardNumberValidationToFail() {
        //Arrange
        val cardNumber = ""

        //Act
        val actualValidationResponse = sut.validateCardNumber(cardNumber)

        //Assert
        assertFalse(actualValidationResponse)
    }

}