package com.ygorxkharo.obey.web.app.presentation.scenes.licensepurchase.utilities

import kotlin.test.Test
import kotlin.test.assertEquals

class PaymentCardFormatterTest {

    @Test
    fun test_When_FormattingCardNumberWithNoSpaces_Expect_SpacesToBeAddedAfterFourNumbers() {
        //Arrange
        val cardNumberTextValue = "5432123423"
        val expectedCardNumberTextValue = "5432 1234 23"

        //Act
        val actualFormattedCardNumber = PaymentCardFormatter.formatCardNumber(cardNumberTextValue)

        //Assert
        assertEquals(expectedCardNumberTextValue, actualFormattedCardNumber)
    }

    @Test
    fun test_When_FormattingCardNumberWithSpaces_Expect_CardNumberToBeFormatted() {
        //Arrange
        val cardNumberTextValue = "5432 1234 23543428"
        val expectedCardNumberTextValue = "5432 1234 2354 3428"

        //Act
        val actualFormattedCardNumber = PaymentCardFormatter.formatCardNumber(cardNumberTextValue)

        //Assert
        assertEquals(expectedCardNumberTextValue, actualFormattedCardNumber)
    }

    @Test
    fun test_When_TheCardNumberLengthIsGreaterThan19_Expect_TheCharacterLengthToHaveAMaxOf23Numbers() {
        //Arrange
        val cardNumberTextValue = "5432123423543212342355"
        val expectedCardNumberTextValue = "5432 1234 2354 3212 342"
        val expectedCardNumberLength = 23

        //Act
        val actualFormattedCardNumber = PaymentCardFormatter.formatCardNumber(cardNumberTextValue)

        //Assert
        assertEquals(expectedCardNumberTextValue, actualFormattedCardNumber)
        assertEquals(expectedCardNumberLength, actualFormattedCardNumber.length)
    }
}