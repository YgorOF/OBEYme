package com.ygorxkharo.obey.web.data.licensepurchase.payment.validation

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MonthYearExpiryDateValidatorTest {

    private val sut = MonthYearExpiryDateValidator()

    @Test
    fun test_When_ValidExpiryDateIsUsed_Expect_ExpiryDateValidationToPass() {
        //Arrange
        val expiryDate = "05/26"
        val dateToday = "2025-07-06T16:04:51.157Z"

        //Act
        val actualValidationResponse = sut.validateExpiryDate(expiryDate, dateToday)

        //Assert
        assertTrue(actualValidationResponse)
    }

    @Test
    fun test_When_InvalidMonthValueInExpiryDateIsUsed_Expect_ExpiryDateValidationToFail() {
        //Arrange
        val expiryDate = "13/25"
        val dateToday = "2025-04-06T16:04:51.157Z"

        //Act
        val actualValidationResponse = sut.validateExpiryDate(expiryDate, dateToday)

        //Assert
        assertFalse(actualValidationResponse)
    }

    @Test
    fun test_When_OutdatedYearValueInExpiryDateIsUsed_Expect_ExpiryDateValidationToFail() {
        //Arrange
        val expiryDate = "11/24"
        val dateToday = "2025-04-06T16:04:51.157Z"

        //Act
        val actualValidationResponse = sut.validateExpiryDate(expiryDate, dateToday)

        //Assert
        assertFalse(actualValidationResponse)
    }

    @Test
    fun test_When_EmptyValueInExpiryDateIsUsed_Expect_ExpiryDateValidationToFail() {
        //Arrange
        val expiryDate = ""
        val dateToday = "2025-04-06T16:04:51.157Z"

        //Act
        val actualValidationResponse = sut.validateExpiryDate(expiryDate, dateToday)

        //Assert
        assertFalse(actualValidationResponse)
    }

}