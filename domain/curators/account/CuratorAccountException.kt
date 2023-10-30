package com.ygorxkharo.obey.web.domain.curators.account

/**
 * Encapsulates errors thrown when accessing an Obey user's account
 */
class CuratorAccountException: Exception {
    /**
     * Provide a message explaining what caused the user account error
     * @param message The message explaining the cause of the error
     */
    constructor(message: String): super(message)

    /**
     * Provide a [Throwable] in the case of chained exceptions
     * @param throwable The throwable containing the original stacktrace explaining the cause of the error
     */
    constructor(throwable: Throwable): super(throwable)
}