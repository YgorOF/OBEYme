package com.ygorxkharo.obey.web.data.licensepurchase.payment.validation

import com.ygorxkharo.obey.web.domain.licensepurchase.payment.validation.model.DebitCreditCardType
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultDebitCreditCardProviderResolverTest {

    private val sut = DefaultDebitCreditCardProviderResolver()

    @Test
    fun test_When_CardNumberMatchesVisa_Expect_VisaCardTypeToBeReturned() {
        //Arrange
        val mastercardCardNumber = "4261 7755 3348 3433"

        //Act
        val cardType = sut.getCardProvider(mastercardCardNumber)

        //Assert
        assertEquals(cardType, DebitCreditCardType.VISA)
    }

    @Test
    fun test_When_CardNumberMatchesMasterCard_Expect_MasterCardCardTypeToBeReturned() {
        //Arrange
        val visaCardNumber = "542"

        //Act
        val cardType = sut.getCardProvider(visaCardNumber)

        //assert
        assertEquals(cardType, DebitCreditCardType.MASTERCARD)
    }

    @Test
    fun test_When_CardNumberDoesNotMatchVisaOrMasterCard_Expect_UnknownCardTypeToBeReturned() {
        //Arrange
        val americanExpressCardNumber = "374"

        //Act
        val cardType = sut.getCardProvider(americanExpressCardNumber)

        //Assert
        assertEquals(cardType, DebitCreditCardType.UNKNOWN)
    }
}