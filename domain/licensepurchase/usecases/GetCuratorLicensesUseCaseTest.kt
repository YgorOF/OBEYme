package com.ygorxkharo.obey.web.domain.licensepurchase.usecases

import com.ygorxkharo.obey.web.domain.common.Result
import com.ygorxkharo.obey.web.domain.licensepurchase.CuratorLicenseService
import com.ygorxkharo.obey.web.domain.licensepurchase.model.CuratorLicense
import kotlin.test.Test
import kotlin.test.assertTrue

class GetCuratorLicensesUseCaseTest {

    private var isCuratorLicenseServiceCalled = false
    private val curatorLicenseService = object: CuratorLicenseService {
        override fun getCuratorLicensesAsync(onComplete: (Result<List<CuratorLicense>>) -> Unit) {
            isCuratorLicenseServiceCalled = true
        }
    }

    @Test
    fun test_When_GetCuratorLicenseUseCaseIsInvoked_Expect_TheCuratorLicenseServiceToBeCalled() {
        //Arrange
        val sut = GetCuratorLicensesUseCase(curatorLicenseService)
        val onGetLicensesCallback: (Result<List<CuratorLicense>>) -> Unit = {}

        //Act
        sut.invoke(onGetLicensesCallback)

        //Assert
        assertTrue(isCuratorLicenseServiceCalled)
    }
}