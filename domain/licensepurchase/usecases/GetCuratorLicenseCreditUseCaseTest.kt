package com.ygorxkharo.obey.web.domain.licensepurchase.usecases

import com.ygorxkharo.obey.web.domain.common.Result
import com.ygorxkharo.obey.web.domain.creditfacility.CreditFacilityService
import com.ygorxkharo.obey.web.domain.creditfacility.activation.model.CuratorLicenseCreditRequest
import com.ygorxkharo.obey.web.domain.creditfacility.activation.model.CuratorLicenseCreditResult
import com.ygorxkharo.obey.web.domain.creditfacility.activation.usecases.GetCuratorLicenseCreditUseCase
import kotlin.test.Test
import kotlin.test.assertTrue

class GetCuratorLicenseCreditUseCaseTest {

    private var isCreditFacilityServiceCalled = false
    private val creditFacilityService = object: CreditFacilityService {
        override fun getCreditByLicenseTypeAsync(
            curatorLicenseCreditRequest: CuratorLicenseCreditRequest,
            onComplete: (Result<CuratorLicenseCreditResult>) -> Unit
        ) {
            isCreditFacilityServiceCalled = true
        }
    }

    @Test
    fun test_When_UseCaseIsInvoked_Expect_TheCreditFacilityServiceToBeCalled() {
        //Arrange
        val sut = GetCuratorLicenseCreditUseCase(creditFacilityService)
        val onGetLicensesCreditCallback: (Result<CuratorLicenseCreditResult>) -> Unit = {}
        val userId = "p.kjsfaljadlfad"
        val licenseTypeName = "Local curator license"
        val curatorLicenseCreditRequest = CuratorLicenseCreditRequest(userId, licenseTypeName)

        //Act
        sut.invoke(curatorLicenseCreditRequest, onGetLicensesCreditCallback)

        //Assert
        assertTrue(isCreditFacilityServiceCalled)
    }

}