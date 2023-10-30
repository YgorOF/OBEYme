package com.ygorxkharo.obey.web.app.presentation.scenes.licensepurchase.payment.viewmodel

import com.ygorxkharo.obey.web.domain.licensepurchase.payment.CardProviderLogoResolver
import com.ygorxkharo.obey.web.domain.licensepurchase.payment.DebitCreditCardProviderResolver
import com.ygorxkharo.obey.web.domain.licensepurchase.payment.usecases.GetCardProviderLogoUseCase
import com.ygorxkharo.obey.web.domain.licensepurchase.payment.validation.ExpiryDateValidator
import com.ygorxkharo.obey.web.domain.licensepurchase.payment.validation.PaymentCardDetailsValidator
import com.ygorxkharo.obey.web.domain.licensepurchase.payment.validation.model.DebitCreditCardType
import com.ygorxkharo.obey.web.domain.licensepurchase.payment.validation.usecases.GetDebitCardProvider
import com.ygorxkharo.obey.web.domain.licensepurchase.payment.validation.usecases.ValidateCardNumberUseCase
import com.ygorxkharo.obey.web.domain.licensepurchase.payment.validation.usecases.ValidateExpiryDateUseCase
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DefaultCuratorLicensePaymentEntryFormViewModelTest {

    private lateinit var actualCardProviderLogo: String
    private val paymentCardProviderLogoObserver: (String) -> Unit = {
        actualCardProviderLogo = it
    }

    private var actualCardNumberValidationResult = false
    private val cardNumberValidationObserver: (Boolean) -> Unit = {
        actualCardNumberValidationResult = it
    }

    private var actualExpiryDateValidationResult = false
    private val expiryDateValidationObserver: (Boolean) -> Unit = {
        actualExpiryDateValidationResult = it
    }

    private val getCardProviderUseCase = stubGetCardProviderUseCase(DebitCreditCardType.MASTERCARD)
    private val masterCardLogoUrl = "images/logo/mastercard.png"
    private val getCardProviderLogoUseCase = stubGetCardProviderLogoUseCase(masterCardLogoUrl)
    private lateinit var sut: DefaultCuratorLicensePaymentEntryFormViewModel

    @BeforeTest
    fun setup() {
        actualCardProviderLogo = ""
        actualCardNumberValidationResult = false
        actualExpiryDateValidationResult = false
    }

    @Test
    fun test_When_updatingTheCardProviderLogo_Expect_ThePaymentCardProviderLogoObserverToTriggerWithALogoURL() {
        //Arrange
        val validateCardNumberUseCase = stubValidateCardNumberUseCase(true)
        val validateExpiryDateUseCase = stubValidateExpiryDateUseCase(isExpiryDateValid = true)
        sut = DefaultCuratorLicensePaymentEntryFormViewModel(
            getCardProviderUseCase,
            getCardProviderLogoUseCase,
            validateCardNumberUseCase,
            validateExpiryDateUseCase
        )
        sut.paymentCardProviderLogoObserver = paymentCardProviderLogoObserver

        val expectedCardProviderUrl = masterCardLogoUrl
        val cardNumber = "5263 23"

        //Act
        sut.updatePaymentCardLogo(cardNumber)

        //Assert
        assertEquals(expectedCardProviderUrl, actualCardProviderLogo)
    }

    @Test
    fun test_When_CardValidationSucceeds_Expect_TheCardNumberValidationObserverToTriggerWithTrue() {
        //Arrange
        val validateCardNumberUseCase = stubValidateCardNumberUseCase(true)
        val validateExpiryDateUseCase = stubValidateExpiryDateUseCase(isExpiryDateValid = true)
        sut = DefaultCuratorLicensePaymentEntryFormViewModel(
            getCardProviderUseCase,
            getCardProviderLogoUseCase,
            validateCardNumberUseCase,
            validateExpiryDateUseCase
        )
        sut.paymentCardProviderLogoObserver = paymentCardProviderLogoObserver
        sut.cardNumberValidationObserver = cardNumberValidationObserver

        val cardNumber = "5263 2334 3843 7574"

        //Act
        sut.validateCardNumber(cardNumber)

        //Assert
        assertTrue(actualCardNumberValidationResult)
    }

    @Test
    fun test_When_CardValidationFails_Expect_TheCardNumberValidationObserverToTriggerWithFalse() {
        //Arrange
        val validateCardNumberUseCase = stubValidateCardNumberUseCase(false)
        val validateExpiryDateUseCase = stubValidateExpiryDateUseCase(isExpiryDateValid = true)
        sut = DefaultCuratorLicensePaymentEntryFormViewModel(
            getCardProviderUseCase,
            getCardProviderLogoUseCase,
            validateCardNumberUseCase,
            validateExpiryDateUseCase
        )
        sut.paymentCardProviderLogoObserver = paymentCardProviderLogoObserver
        sut.cardNumberValidationObserver = cardNumberValidationObserver

        val cardNumber = "5111 8754 2422 9984"

        //Act
        sut.validateCardNumber(cardNumber)

        //Assert
        assertFalse(actualCardNumberValidationResult)
    }

    @Test
    fun test_When_ExpiryDateValidationSucceeds_Expect_TheExpiryDateValidationObserverToTriggerWithTrue() {
        //Arrange
        val validateCardNumberUseCase = stubValidateCardNumberUseCase(isCardNumberValid = false)
        val validateExpiryDateUseCase = stubValidateExpiryDateUseCase(isExpiryDateValid = true)
        sut = DefaultCuratorLicensePaymentEntryFormViewModel(
            getCardProviderUseCase,
            getCardProviderLogoUseCase,
            validateCardNumberUseCase,
            validateExpiryDateUseCase
        )
        sut.expiryDateValidationObserver = expiryDateValidationObserver

        val expiryDate = "12/28"

        //Act
        sut.validateExpiryDate(expiryDate)

        //Assert
        assertTrue(actualExpiryDateValidationResult)
    }

    @Test
    fun test_When_ExpiryDateValidationFails_Expect_TheExpiryDateValidationObserverToTriggerWithFalse() {
        //Arrange
        val validateCardNumberUseCase = stubValidateCardNumberUseCase(isCardNumberValid = false)
        val validateExpiryDateUseCase = stubValidateExpiryDateUseCase(isExpiryDateValid = false)
        sut = DefaultCuratorLicensePaymentEntryFormViewModel(
            getCardProviderUseCase,
            getCardProviderLogoUseCase,
            validateCardNumberUseCase,
            validateExpiryDateUseCase
        )
        sut.expiryDateValidationObserver = expiryDateValidationObserver
        val expiryDate = "12/19"

        //Act
        sut.validateExpiryDate(expiryDate)

        //Assert
        assertFalse(actualExpiryDateValidationResult)
    }

    private fun stubGetCardProviderUseCase(cardType: DebitCreditCardType): GetDebitCardProvider {
        val cardProviderResolver = object: DebitCreditCardProviderResolver {
            override fun getCardProvider(cardNumber: String): DebitCreditCardType {
                return cardType
            }
        }
        return GetDebitCardProvider(cardProviderResolver)
    }

    private fun stubGetCardProviderLogoUseCase(cardProviderLogoUrl: String): GetCardProviderLogoUseCase {
        val cardProviderLogoResolver = object: CardProviderLogoResolver {
            override fun resolveCardProviderLogo(debitCreditCardType: DebitCreditCardType): String {
                return cardProviderLogoUrl
            }
        }

        return GetCardProviderLogoUseCase(cardProviderLogoResolver)
    }

    private fun stubValidateCardNumberUseCase(isCardNumberValid: Boolean): ValidateCardNumberUseCase {
        val cardNumberValidator = object: PaymentCardDetailsValidator {
            override fun validateCardNumber(inputValue: String): Boolean {
                return isCardNumberValid
            }
        }
        return ValidateCardNumberUseCase(cardNumberValidator)
    }

    private fun stubValidateExpiryDateUseCase(isExpiryDateValid: Boolean): ValidateExpiryDateUseCase {
        val expiryDateValidator = object: ExpiryDateValidator {
            override fun validateExpiryDate(inputValue: String, timeNowStringValue: String): Boolean {
                return isExpiryDateValid
            }
        }
        return ValidateExpiryDateUseCase(expiryDateValidator)
    }

}