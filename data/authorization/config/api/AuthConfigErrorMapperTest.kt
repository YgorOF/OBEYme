package com.ygorxkharo.obey.web.data.authorization.config.api

import com.ygorxkharo.obey.web.data.authorization.MusicPlatformAuthorizationException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AuthConfigErrorMapperTest {

    @Test
    fun test_When_MappingErrorBodyFromAuthConfigHandler_Expect_MusicAuthorizationPlatformExceptionToBeReturned() {
        //Arrange
        val expectedErrorMessage = "Error getting access token"
        val errorBody = """
            {
                "error": "$expectedErrorMessage"
            }
        """.trimIndent()
        val sut = AuthConfigErrorMapper()

        //Act
        val actualException = sut.mapToException(errorBody)

        //Assert
        assertTrue(actualException is MusicPlatformAuthorizationException)
        assertEquals(expectedErrorMessage, actualException.message)
    }

}