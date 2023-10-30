package com.ygorxkharo.obey.web.app.presentation.scenes.licensepurchase.utilities

import kotlin.test.Test
import kotlin.test.assertEquals

class DiscountCalculatorTest {

    @Test
    fun test_When_FinalPriceIsCalculated_Expect_TheDiscountedPriceToBeReturnedAsAString() {
        //Arrange
        val currentPrice = 9.45
        val discountAmount = 5.10
        val expectedFinalPrice = "4.35"

        //Act
        val actualFinalPrice = DiscountCalculator.calculateAsString(currentPrice, discountAmount)

        //Assert
        assertEquals(expectedFinalPrice, actualFinalPrice)
    }
}