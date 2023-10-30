package com.ygorxkharo.obey.web.app.presentation.scenes.licensepurchase.utilities

import kotlin.test.Test
import kotlin.test.assertEquals

class ExpiryDateFormatterTest {

    @Test
    fun test_When_TheExpiryDateIs1CharacterLong_ExpectNoSlashAtTheEnd() {
        //Arrange
        val expiryDateInputValue = "1"
        val expectedExpiryDateTextValue = "1"

        //Act
        val formattedExpiryDate = ExpiryDateFormatter.formatExpiryDate(expiryDateInputValue)

        //Assert
        assertEquals(expectedExpiryDateTextValue, formattedExpiryDate)
    }

    @Test
    fun test_When_TheExpiryDateIs2CharactersLong_ExpectASlashAtTheEnd() {
        //Arrange
        val expiryDateInputValue = "10"
        val expectedExpiryDateTextValue = "10/"

        //Act
        val formattedExpiryDate = ExpiryDateFormatter.formatExpiryDate(expiryDateInputValue)

        //Assert
        assertEquals(expectedExpiryDateTextValue, formattedExpiryDate)
    }

    @Test
    fun test_When_TheExpiryDateIsMoreThan3DigitsLong_TheInputValueToBeReturnedUnchanged() {
        //Arrange
        val expiryDateInputValue = "10/21"
        val expectedExpiryDateTextValue = "10/21"

        //Act
        val formattedExpiryDate = ExpiryDateFormatter.formatExpiryDate(expiryDateInputValue)

        //Assert
        assertEquals(expectedExpiryDateTextValue, formattedExpiryDate)
    }

}