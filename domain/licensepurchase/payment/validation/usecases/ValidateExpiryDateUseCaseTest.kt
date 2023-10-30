package com.ygorxkharo.obey.web.domain.licensepurchase.payment.validation.usecases

import com.ygorxkharo.obey.web.domain.licensepurchase.payment.validation.ExpiryDateValidator
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ValidateExpiryDateUseCaseTest {

    @Test
    fun test_When_expiryDatePassesValidation_Expect_TrueToBeReturned() {
        //Arrange
        val expiryDateValidator = stubExpiryDateValidator(isValidExpiryDate = true)
        val sut = ValidateExpiryDateUseCase(expiryDateValidator)
        val expiryDate = "12/28"

        //Act
        val actualValidationResult = sut.invoke(expiryDate)

        //Assert
        assertTrue(actualValidationResult)
    }

    @Test
    fun test_When_expiryDateFailsValidation_Expect_FalseToBeReturned() {
        //Arrange
        val expiryDateValidator = stubExpiryDateValidator(isValidExpiryDate = false)
        val sut = ValidateExpiryDateUseCase(expiryDateValidator)
        val expiryDate = "15/28"

        //Act
        val actualValidationResult = sut.invoke(expiryDate)

        //Assert
        assertFalse(actualValidationResult)
    }

    private fun stubExpiryDateValidator(isValidExpiryDate: Boolean): ExpiryDateValidator {
        return object: ExpiryDateValidator {
            override fun validateExpiryDate(inputValue: String, timeNowStringValue: String): Boolean {
                return isValidExpiryDate
            }
        }
    }

}