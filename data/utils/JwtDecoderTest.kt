package com.ygorxkharo.obey.web.data.utils

import com.ygorxkharo.obey.web.data.users.authentication.UserAuthenticationException
import kotlin.test.Test
import kotlin.test.assertEquals

class JwtDecoderTest {

    private val sut = DefaultJwtDecoder()

    @Test
    fun test_When_TokenIsDecoded_Expect_TheJsonOutputToContainTheUserEmail() {
        //Arrange
        val accessToken = "eyJraWQiOiI2VVRXRDRFYVZ0ZHNxNmVRamVDdVwvakdCWXVyWmduSnVGXC96U2VhWDR3TUU9IiwiYWxnIjoiUlMyNTYifQ.eyJhdF9oYXNoIjoicE1qU3NsalcxLVl6VzdXTnFHTW5HQSIsInN1YiI6IjMzNjNiNGJmLWUyM2YtNGI3ZC04ZDJiLTZhOWRjNjQwZGU5OCIsImNvZ25pdG86Z3JvdXBzIjpbImV1LXdlc3QtMl9CWlZXaHNUWTJfU2lnbkluV2l0aEFwcGxlIl0sImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwiaXNzIjoiaHR0cHM6XC9cL2NvZ25pdG8taWRwLmV1LXdlc3QtMi5hbWF6b25hd3MuY29tXC9ldS13ZXN0LTJfQlpWV2hzVFkyIiwiY29nbml0bzp1c2VybmFtZSI6IlNpZ25JbldpdGhBcHBsZV8wMDA0MzguYzUyZDU2OTJhMDFkNGY4Yjk3MWY3YWUyOTYzN2EzNmIuMTM1OCIsImF1ZCI6IjNwMWYyNG8zaXBqaG9zZHFmdTlkcGtlbXJkIiwiaWRlbnRpdGllcyI6W3sidXNlcklkIjoiMDAwNDM4LmM1MmQ1NjkyYTAxZDRmOGI5NzFmN2FlMjk2MzdhMzZiLjEzNTgiLCJwcm92aWRlck5hbWUiOiJTaWduSW5XaXRoQXBwbGUiLCJwcm92aWRlclR5cGUiOiJTaWduSW5XaXRoQXBwbGUiLCJpc3N1ZXIiOm51bGwsInByaW1hcnkiOiJ0cnVlIiwiZGF0ZUNyZWF0ZWQiOiIxNjIzOTM4MzI1NjgxIn1dLCJ0b2tlbl91c2UiOiJpZCIsImF1dGhfdGltZSI6MTYyNDgyODgwMywiZXhwIjoxNjI0ODMyNDAzLCJpYXQiOjE2MjQ4Mjg4MDMsImVtYWlsIjoicm9iZXJ0QGJsZWVwdHYuY28ifQ.yJTIT0scIh0n5DXiXindm-SA4bWzwChGkJiX0mvI0EU9QIocGm1Hocdkp6Jz5zasyvTLPfZazB53mWIrJjyLxUdLNlYwDDXnau9KQCqNNPHqDSa9GK9mdvr95V_ueHl1MLyFaHaX2Y4AFWze_4n88q7Luotl13sVklD69bIajLa8BmGeB-er3VRfRQabAm7ZUniwtHq-dk0ckO6ZPqltSevTuxMP_B5KX_ZjMDFyIXxjjb0RTsf561po6KzSDjC7ZyRNL7EqFv01_7HqOHO999O50fM-xyBUl-ORjsJAN707InDSwqUs3UTREvl0RwD92V-hG7iGK-QWAaM5MjaB0w"
        val expectedEmail = "robert@bleeptv.co"

        //Act
        val actualJWTJSonPayload = sut.decode(accessToken)

        //Assert
        val emailJsonKey = "email"
        assertEquals(expectedEmail, actualJWTJSonPayload[emailJsonKey])
    }

    @Test
    fun test_When_InvalidTokenIsProvided_Expect_AnExceptionToBeThrown() {
        //Arrange
        val emptyErrorMessage = ""
        val accessToken = ""
        val expectedErrorMessage = "Invalid token provided for decoding"

        //Act
        val actualErrorMessage = try {
            sut.decode(accessToken)
            emptyErrorMessage
        } catch (ex: IllegalArgumentException) {
            ex.message
        }

        //Assert
        assertEquals(expectedErrorMessage, actualErrorMessage)
    }

}