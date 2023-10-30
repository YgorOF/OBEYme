package com.ygorxkharo.obey.web.app.data.testing.authorization

import com.ygorxkharo.obey.web.domain.authentication.AuthorizationService
import com.ygorxkharo.obey.web.domain.authentication.model.AuthorizationResult
import com.ygorxkharo.obey.web.domain.common.Result

/**
 * Testing fixture to stub the outputs for the authorization service interface
 */
object AuthorizationServiceFixture {

    /**
     * In the absence of a mocking framework, we had to resort to creating stubs of the authorization repository manually
     *
     * @param platformNameValueListener Captures the value for the platform name passed into the repository
     * @param authResult The value of the authorization result the authorization repository should return
     * @return an instance of an [AuthorizationService] pre-configured with an authorization result
     */
    fun stubAuthorizationService(
        platformNameValueListener: (String) -> Unit,
        authResult: Result<AuthorizationResult>
    ): AuthorizationService {
        return object: AuthorizationService {
            override fun getUserAuthorization(
                platformName: String,
                onComplete: (Result<AuthorizationResult>) -> Unit
            ) {
                platformNameValueListener.invoke(platformName)
                onComplete.invoke(authResult)
            }
        }
    }
}