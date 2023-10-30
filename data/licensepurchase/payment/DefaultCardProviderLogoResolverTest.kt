package com.ygorxkharo.obey.web.data.licensepurchase.payment

import com.ygorxkharo.obey.web.domain.licensepurchase.payment.validation.model.DebitCreditCardType
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultCardProviderLogoResolverTest {

    private val masterCardLogoUrl = "images/logo/mastercard.png"
    private val visaLogoUrl = "images/logo/visa.png"
    private val unknownCardUrl = ""

    private val cardLogoCollection = mapOf(
        DebitCreditCardType.MASTERCARD to masterCardLogoUrl,
        DebitCreditCardType.VISA to visaLogoUrl,
        DebitCreditCardType.UNKNOWN to unknownCardUrl
    )

    private val sut = DefaultCardProviderLogoResolver(cardLogoCollection)

    @Test
    fun test_When_MasterCardTypeIsProvided_Expect_MasterCardLogoUrlToBeReturned() {
        //Arrange
        val expectedCardLogoUrl = masterCardLogoUrl

        //Act
        val actualCardLogoUrl = sut.resolveCardProviderLogo(DebitCreditCardType.MASTERCARD)

        //Assert
        assertEquals(expectedCardLogoUrl, actualCardLogoUrl)
    }

    @Test
    fun test_When_VisaCardTypeIsProvided_Expect_VisaLogoUrlToBeReturned() {
        //Arrange
        val expectedCardLogoUrl = visaLogoUrl

        //Act
        val actualCardLogoUrl = sut.resolveCardProviderLogo(DebitCreditCardType.VISA)

        //Assert
        assertEquals(expectedCardLogoUrl, actualCardLogoUrl)
    }

    @Test
    fun test_When_UnknownCardTypeIsProvided_Expect_EmptyValueToBeReturned() {
        //Arrange
        val expectedCardLogoUrl = unknownCardUrl

        //Act
        val actualCardLogoUrl = sut.resolveCardProviderLogo(DebitCreditCardType.UNKNOWN)

        //Assert
        assertEquals(expectedCardLogoUrl, actualCardLogoUrl)
    }

}