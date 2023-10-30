package com.ygorxkharo.obey.web.data.creditfacility.offer

import com.ygorxkharo.obey.web.data.creditfacility.common.StreamingEngagement
import kotlin.test.Test
import kotlin.test.assertEquals

class CreditsOfferHeadingResolverTest {

    @Test
    fun test_When_TheUserIsSerious_Expect_TheMessageToDisplayTheSeriousUserHeading() {
        //Arrange
        val expectedHeadingText = "You’re a serious music lover"
        val streamingEngagement = StreamingEngagement.SERIOUS_USER

        //Act
        val actualHeadingText = CreditsOfferHeadingResolver.resolveHeadingText(streamingEngagement)

        //Assert
        assertEquals(expectedHeadingText, actualHeadingText)
    }

    @Test
    fun test_When_TheUserIsDope_Expect_TheMessageToDisplayTheDopeUserHeading() {
        //Arrange
        val expectedHeadingText = "You’re creating dope shit."
        val streamingEngagement = StreamingEngagement.DOPE_USER

        //Act
        val actualHeadingText = CreditsOfferHeadingResolver.resolveHeadingText(streamingEngagement)

        //Assert
        assertEquals(expectedHeadingText, actualHeadingText)
    }

    @Test
    fun test_When_TheUserIsAvid_Expect_TheMessageToDisplayTheAvidUserHeading() {
        //Arrange
        val expectedHeadingText = "You’re an avid listener."
        val streamingEngagement = StreamingEngagement.AVID_USER

        //Act
        val actualHeadingText = CreditsOfferHeadingResolver.resolveHeadingText(streamingEngagement)

        //Assert
        assertEquals(expectedHeadingText, actualHeadingText)
    }

}