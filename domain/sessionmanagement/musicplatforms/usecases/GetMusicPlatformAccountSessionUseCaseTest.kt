package com.ygorxkharo.obey.web.domain.sessionmanagement.musicplatforms.usecases

import com.ygorxkharo.obey.web.domain.common.Result
import com.ygorxkharo.obey.web.domain.sessionmanagement.musicplatforms.MusicPlatformAccountService
import com.ygorxkharo.obey.web.domain.sessionmanagement.musicplatforms.model.MusicPlatformAccountSession
import com.ygorxkharo.obey.web.domain.sessionmanagement.musicplatforms.model.MusicPlatformAccountSessionRequest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetMusicPlatformAccountSessionUseCaseTest {

    private lateinit var actualUserAccountSessionRequest: MusicPlatformAccountSessionRequest
    private lateinit var actualGetUserAccountCallback: (Result<MusicPlatformAccountSession>) -> Unit

    private val userAccountService = object: MusicPlatformAccountService {
        override fun getUserAccountAsync(
            musicPlatformAccountSessionRequest: MusicPlatformAccountSessionRequest,
            onGetUserAccountSession: (Result<MusicPlatformAccountSession>) -> Unit
        ) {
            actualUserAccountSessionRequest = musicPlatformAccountSessionRequest
            actualGetUserAccountCallback = onGetUserAccountSession
        }
    }

    @Test
    fun test_When_TheGetUserAccountSessionUseCaseIsInvoked_Expect_TheUserAccountServiceToBeCalled() {
        //Arrange
        val onGetUserAccount: (Result<MusicPlatformAccountSession>) -> Unit = {}
        val accessToken = "ey.lwkjlkjslkfjl2jrkljslkfjalkjfakljfds"
        val musicPlatform = "test"
        val userAccessSessionRequest = MusicPlatformAccountSessionRequest(
            musicPlatformAccountAccessToken = accessToken,
            musicPlatform = musicPlatform
        )
        val sut = GetMusicPlatformAccountSessionUseCase(userAccountService)

        //Act
        sut.invoke(userAccessSessionRequest, onGetUserAccount)

        //Assert
        assertEquals(onGetUserAccount, actualGetUserAccountCallback)
        assertEquals(userAccessSessionRequest, actualUserAccountSessionRequest)
    }
}