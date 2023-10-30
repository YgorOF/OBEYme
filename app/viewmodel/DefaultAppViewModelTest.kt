package com.ygorxkharo.obey.web.app.viewmodel

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultAppViewModelTest {

    private val sut = DefaultAppViewModel()
    private var actualBackgroundAudioStateValue: Boolean = false
    private val backgroundAudioStateListener: (Boolean) -> Unit = {
        actualBackgroundAudioStateValue = it
    }

    private var actualSignUpOptionsVisibilityStateValue: Boolean = false
    private val signUpOptionsVisibilityStateListener: (Boolean) -> Unit = {
        actualSignUpOptionsVisibilityStateValue = it
    }

    @BeforeTest
    fun setup() {
        sut.backgroundAudioState = backgroundAudioStateListener
        sut.signUpOptionsVisibilityState = signUpOptionsVisibilityStateListener

        actualBackgroundAudioStateValue = false
        actualSignUpOptionsVisibilityStateValue = false
    }

    @Test
    fun test_When_BackgroundAudioIsOn_Expect_ToggleToMuteAudio() {
        //Arrange
        val isBackgroundAudioOn = false
        val expectedBackgroundAudioStateValue = true

        //Act
        sut.enableBackgroundAudio(isBackgroundAudioOn)

        //Assert
        assertEquals(expectedBackgroundAudioStateValue, actualBackgroundAudioStateValue)
    }

    @Test
    fun test_When_BackgroundAudioIsOff_Expect_ToggleToUnMuteAudio() {
        //Arrange
        val isBackgroundAudioOn = true
        val expectedBackgroundAudioStateValue = false

        //Act
        sut.enableBackgroundAudio(isBackgroundAudioOn)

        //Assert
        assertEquals(expectedBackgroundAudioStateValue, actualBackgroundAudioStateValue)
    }

    @Test
    fun test_When_ShowSignUpOptionsIsTriggered_Expect_TheSignUpOptionsVisibleStateToBeTrue() {
        //Arrange
        val expectedBackgroundAudioStateValue = true

        //Act
        sut.showSignUpOptions()

        //Assert
        assertEquals(actualSignUpOptionsVisibilityStateValue, expectedBackgroundAudioStateValue)
    }

    @Test
    fun test_When_HideSignUpOptionsIsTriggered_Expect_TheSignUpOptionsVisibleStateToBeFalse() {
        //Arrange
        val expectedBackgroundAudioStateValue = false

        //Act
        sut.hideSignUpOptions()

        //Assert
        assertEquals(actualSignUpOptionsVisibilityStateValue, expectedBackgroundAudioStateValue)
    }
}