package com.ygorxkharo.obey.web.domain.licensepurchase.usecases

import com.ygorxkharo.obey.web.domain.licensepurchase.payment.validation.PaymentCardDetailsValidator
import com.ygorxkharo.obey.web.domain.licensepurchase.payment.validation.usecases.ValidateCardNumberUseCase
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ValidateCardNumberUseCaseTest {

    @Test
    fun test_When_CardNumberPassesValidation_Expect_TrueToBeReturned() {
        //Arrange
        val cardNumberValidator = stubCardNumberValidator(isCardNumberValid = true)
        val sut = ValidateCardNumberUseCase(cardNumberValidator)
        val cardNumber = "5270 1002 4938 3873"

        //Act
        val actualValidationResult = sut.invoke(cardNumber)

        //Assert
        assertTrue(actualValidationResult)
    }

    @Test
    fun test_When_CardNumberFailsValidation_Expect_FallsToBeReturned() {
        //Arrange
        val cardNumberValidator = stubCardNumberValidator(isCardNumberValid = false)
        val sut = ValidateCardNumberUseCase(cardNumberValidator)
        val cardNumber = "5283 2372 4938 3873"

        //Act
        val actualValidationResult = sut.invoke(cardNumber)

        //Assert
        assertFalse(actualValidationResult)
    }

    private fun stubCardNumberValidator(isCardNumberValid: Boolean): PaymentCardDetailsValidator {
        return object: PaymentCardDetailsValidator {
            override fun validateCardNumber(inputValue: String): Boolean {
                return isCardNumberValid
            }
        }
    }

}