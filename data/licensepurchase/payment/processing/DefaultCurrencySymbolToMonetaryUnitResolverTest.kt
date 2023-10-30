package com.ygorxkharo.obey.web.data.licensepurchase.payment.processing

import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultCurrencySymbolToMonetaryUnitResolverTest {

    private val currencyCodeSelection = mapOf("R" to "ZAR")
    private val countryCodeSelection = mapOf("R" to "ZA")
    private val currencySymbolCollection = mapOf("ZAR" to "R")

    private val sut = DefaultCurrencySymbolToMonetaryUnitResolver(
        countryCodeCollection = countryCodeSelection,
        currencyCodeCollection = currencyCodeSelection,
        currencySymbolCollection = currencySymbolCollection
    )

    private val validCurrencySymbol = "R" //South African Rand
    private val invalidCurrencySymbol = "GH₵" //Ghana Cedi
    private val validCurrencyCode = "ZAR"
    private val invalidCurrencyCode = "GHS"

    @Test
    fun test_When_CurrencySymbolExistsForCountryCode_Expect_CountryCodeToBeReturned() {
        //Arrange
        val expectedCountryCode = "ZA"

        //Act
        val actualCountryCode = sut.getCountryCodeFromCurrencySymbol(validCurrencySymbol)

        //Assert
        assertEquals(expectedCountryCode, actualCountryCode)
    }

    @Test
    fun test_When_CurrencySymbolExistsForCurrencyCode_Expect_CurrencyCodeToBeReturned() {
        //Arrange
        val expectedCurrencyCode = "ZAR"

        //Act
        val actualCurrencyCode = sut.getCurrencyCodeFromCurrencySymbol(validCurrencySymbol)

        //Assert
        assertEquals(expectedCurrencyCode, actualCurrencyCode)
    }

    @Test
    fun test_When_CurrencyCodeDoesNotExistsForCountrySymbol_Expect_ExceptionToBeThrown() {
        //Arrange
        val expectedErrorMessage = "No country code available for this currency symbol"

        //Act
        val actualErrorMessage = try {
            sut.getCountryCodeFromCurrencySymbol(invalidCurrencySymbol)
            ""
        } catch (ex: IllegalArgumentException) {
            ex.message
        }

        //Assert
        assertEquals(expectedErrorMessage, actualErrorMessage)
    }

    @Test
    fun test_When_CurrencySymbolDoesNotExistsForCurrencyCode_Expect_ExceptionToBeThrown() {
        //Arrange
        val expectedErrorMessage = "No currency code available for this currency symbol"

        //Act
        val actualErrorMessage = try {
            sut.getCurrencyCodeFromCurrencySymbol(invalidCurrencySymbol)
            ""
        } catch (ex: IllegalArgumentException) {
            ex.message
        }

        //Assert
        assertEquals(expectedErrorMessage, actualErrorMessage)
    }

    @Test
    fun test_When_CurrencyCodeExistsForCurrencySymbol_Expect_CurrencyCodeToBeReturned() {
        //Arrange
        val expectedCurrencySymbol = "R"

        //Act
        val actualCurrencySymbol = sut.getCurrencySymbolFromCurrencyCode(validCurrencyCode)

        //Assert
        assertEquals(expectedCurrencySymbol, actualCurrencySymbol)
    }

    @Test
    fun test_When_CurrencySymbolDoesNotExistsForCountryCode_Expect_ExceptionToBeThrown() {
        //Arrange
        val expectedErrorMessage = "No currency symbol available for this currency code"

        //Act
        val actualErrorMessage = try {
            sut.getCurrencySymbolFromCurrencyCode(invalidCurrencyCode)
            ""
        } catch (ex: IllegalArgumentException) {
            ex.message
        }

        //Assert
        assertEquals(expectedErrorMessage, actualErrorMessage)
    }

}