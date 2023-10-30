package com.ygorxkharo.obey.web.domain.licensepurchase.payment.usecases

import com.ygorxkharo.obey.web.domain.licensepurchase.payment.CardProviderLogoResolver
import com.ygorxkharo.obey.web.domain.licensepurchase.payment.validation.model.DebitCreditCardType
import kotlin.test.Test
import kotlin.test.assertEquals

class GetCardProviderLogoUseCaseTest {

    private val masterCardLogoUrl = "images/logo/mastercard.png"
    private val visaLogoUrl = "images/logo/visa.png"
    private val unknownCardUrl = ""

    @Test
    fun test_When_ProvidingMasterCardType_Expect_TheMasterCardUrlToBeReturned() {
        //Arrange
        val debitCreditCardType = DebitCreditCardType.MASTERCARD
        val expectedCardProviderLogoUrl = masterCardLogoUrl
        val cardProviderResolver = stubCardProviderLogoResolver(debitCreditCardType)
        val sut = GetCardProviderLogoUseCase(cardProviderResolver)

        //Act
        val actualCardProviderLogoUrl = sut.invoke(debitCreditCardType)

        //Assert
        assertEquals(expectedCardProviderLogoUrl, actualCardProviderLogoUrl)
    }

    @Test
    fun test_When_ProvidingVisaType_Expect_TheVisaUrlToBeReturned() {
        //Arrange
        val debitCreditCardType = DebitCreditCardType.VISA
        val expectedCardProviderLogoUrl = visaLogoUrl
        val cardProviderResolver = stubCardProviderLogoResolver(debitCreditCardType)
        val sut = GetCardProviderLogoUseCase(cardProviderResolver)

        //Act
        val actualCardProviderLogoUrl = sut.invoke(debitCreditCardType)

        //Assert
        assertEquals(expectedCardProviderLogoUrl, actualCardProviderLogoUrl)
    }

    @Test
    fun test_When_ProvidingUnknownCardType_Expect_TheUnknownCardUrlToBeReturned() {
        //Arrange
        val debitCreditCardType = DebitCreditCardType.UNKNOWN
        val expectedCardProviderLogoUrl = unknownCardUrl
        val cardProviderResolver = stubCardProviderLogoResolver(debitCreditCardType)
        val sut = GetCardProviderLogoUseCase(cardProviderResolver)

        //Act
        val actualCardProviderLogoUrl = sut.invoke(debitCreditCardType)

        //Assert
        assertEquals(expectedCardProviderLogoUrl, actualCardProviderLogoUrl)
    }

    private fun stubCardProviderLogoResolver(creditCardType: DebitCreditCardType): CardProviderLogoResolver {

        return object: CardProviderLogoResolver {
            override fun resolveCardProviderLogo(debitCreditCardType: DebitCreditCardType): String {
                return when(creditCardType) {
                    DebitCreditCardType.MASTERCARD -> masterCardLogoUrl
                    DebitCreditCardType.VISA -> visaLogoUrl
                    DebitCreditCardType.UNKNOWN -> unknownCardUrl
                }
            }
        }
    }
}