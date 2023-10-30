package com.ygorxkharo.obey.web.domain.licensepurchase.payment.validation.usecases

import com.ygorxkharo.obey.web.domain.licensepurchase.payment.DebitCreditCardProviderResolver
import com.ygorxkharo.obey.web.domain.licensepurchase.payment.validation.model.DebitCreditCardType
import kotlin.test.Test
import kotlin.test.assertEquals

class GetDebitCardProviderTest {

    @Test
    fun test_When_GettingCardProviderForMasterCardNumber_Expect_UseCaseToReturnMasterCardType() {
        //Arrange
        val cardNumber = "1111 "
        val expectedCreditCardType = DebitCreditCardType.MASTERCARD
        val debitCreditCardProviderResolver = stubDebitCreditCardProviderResolver(expectedCreditCardType)
        val sut = GetDebitCardProvider(debitCreditCardProviderResolver)

        //Act
        val actualDebitCreditCardType = sut.invoke(cardNumber)

        //Assert
        assertEquals(actualDebitCreditCardType, expectedCreditCardType)
    }

    @Test
    fun test_When_GettingCardProviderForVisaNumber_Expect_UseCaseToReturnVisaType() {
        //Arrange
        val cardNumber = "426 "
        val expectedCreditCardType = DebitCreditCardType.VISA
        val debitCreditCardProviderResolver = stubDebitCreditCardProviderResolver(expectedCreditCardType)
        val sut = GetDebitCardProvider(debitCreditCardProviderResolver)

        //Act
        val actualDebitCreditCardType = sut.invoke(cardNumber)

        //Assert
        assertEquals(actualDebitCreditCardType, expectedCreditCardType)
    }

    @Test
    fun test_When_GettingCardProviderForUnknownNumber_Expect_UseCaseToReturnUnknownType() {
        //Arrange
        val cardNumber = "8974 "
        val expectedCreditCardType = DebitCreditCardType.UNKNOWN
        val debitCreditCardProviderResolver = stubDebitCreditCardProviderResolver(expectedCreditCardType)
        val sut = GetDebitCardProvider(debitCreditCardProviderResolver)

        //Act
        val actualDebitCreditCardType = sut.invoke(cardNumber)

        //Assert
        assertEquals(actualDebitCreditCardType, expectedCreditCardType)
    }

    private fun stubDebitCreditCardProviderResolver(cardProvider: DebitCreditCardType): DebitCreditCardProviderResolver  {
        return object: DebitCreditCardProviderResolver {
            override fun getCardProvider(cardNumber: String): DebitCreditCardType {
                return cardProvider
            }
        }
    }

}