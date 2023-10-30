package com.ygorxkharo.obey.web.data.users.authentication.sso.session

import com.ygorxkharo.obey.web.data.users.authentication.UserAuthenticationException
import com.ygorxkharo.obey.web.data.users.authentication.sso.session.model.SSOAccountSessionResult
import com.ygorxkharo.obey.web.data.users.authentication.sso.session.model.SSOSessionRequest
import com.ygorxkharo.obey.web.domain.common.Failure
import com.ygorxkharo.obey.web.domain.sessionmanagement.musicplatforms.model.MusicPlatformAccountSession
import com.ygorxkharo.obey.web.domain.sessionmanagement.musicplatforms.model.MusicPlatformAccountSessionRequest
import com.ygorxkharo.obey.web.domain.common.Result
import com.ygorxkharo.obey.web.domain.common.Success
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DefaultMusicPlatformAccountServiceTest {

    private val validPlatformName = "test"
    private val musicPlatformAccessToken = "ey.slfj30sfklajflkjdsflajowho2hrolshsflkhaf"
    private val musicPlatformAccountSessionRequest = MusicPlatformAccountSessionRequest(
        musicPlatform = validPlatformName,
        musicPlatformAccountAccessToken = musicPlatformAccessToken
    )

    private lateinit var actualUserAccountSessionResult: Result<MusicPlatformAccountSession>
    private val onGetUserAccountSession: (Result<MusicPlatformAccountSession>) -> Unit = {
        actualUserAccountSessionResult = it
    }

    private val musicPlatformAccountCollection = HashMap<String, SSOPlatformUserAccountService>()

    @BeforeTest
    fun setup() {
        musicPlatformAccountCollection.clear()
    }

    @Test
    fun test_When_InvalidPlatformNameIsProvided_Expect_ExceptionIsThrown() {
        //Arrange
        val emptyErrorMessage = ""
        val expectedErrorMessage = "Invalid music platform selected"
        val sut = DefaultMusicPlatformAccountService(musicPlatformAccountCollection)

        //Act
        val actualErrorMessage = try {
            sut.getUserAccountAsync(musicPlatformAccountSessionRequest, onGetUserAccountSession)
            emptyErrorMessage
        } catch(ex: UserAuthenticationException) {
            ex.message
        }

        //Assert
        assertEquals(expectedErrorMessage, actualErrorMessage)
    }

    @Test
    fun test_When_UserAccountSessionIsAcquiredSuccessfully_Expect_OnGetUserAccountAsyncToTriggerWithSuccess() {
        //Arrange
        val expectedUserName = "test_user"
        val ssoSessionResult = SSOAccountSessionResult(username = expectedUserName)
        val musicPlatformAccountService = stubMusicPlatformAccountService(responsePayload = ssoSessionResult, responseError = null)
        musicPlatformAccountCollection[validPlatformName] = musicPlatformAccountService
        val sut = DefaultMusicPlatformAccountService(musicPlatformAccountCollection)

        //Act
        sut.getUserAccountAsync(musicPlatformAccountSessionRequest, onGetUserAccountSession)

        //Assert
        assertTrue(actualUserAccountSessionResult is Success)
        val userAccountSessionResult = actualUserAccountSessionResult as Success
        assertEquals(expectedUserName, userAccountSessionResult.payload.username)
    }

    @Test
    fun test_When_UserAccountSessionRetrievalFails_Expect_OnGetUserAccountAsyncToTriggerWithFailure() {
        //Arrange
        val expectedErrorMessage = "test_user"
        val responseError = UserAuthenticationException(expectedErrorMessage)
        val musicPlatformAccountService = stubMusicPlatformAccountService(responsePayload = null, responseError = responseError)
        musicPlatformAccountCollection[validPlatformName] = musicPlatformAccountService
        val sut = DefaultMusicPlatformAccountService(musicPlatformAccountCollection)

        //Act
        sut.getUserAccountAsync(musicPlatformAccountSessionRequest, onGetUserAccountSession)

        //Assert
        assertTrue(actualUserAccountSessionResult is Failure)
        val userAccountSessionResult = actualUserAccountSessionResult as Failure
        assertEquals(expectedErrorMessage, userAccountSessionResult.error.message)
    }

    private fun stubMusicPlatformAccountService(
        responsePayload: SSOAccountSessionResult?,
        responseError: Exception?
    ): SSOPlatformUserAccountService {
        return object: SSOPlatformUserAccountService {
            override fun getCurrentUserSession(
                ssoSessionRequest: SSOSessionRequest,
                onCompleted: (Result<SSOAccountSessionResult>) -> Unit
            ) {

                if(responsePayload != null) {
                    onCompleted.invoke(Success(responsePayload))
                }

                if(responseError != null) {
                    onCompleted.invoke(Failure(responseError))
                }
            }
        }
    }
}