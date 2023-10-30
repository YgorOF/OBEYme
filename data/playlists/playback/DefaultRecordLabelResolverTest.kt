package com.ygorxkharo.obey.web.data.playlists.playback

import com.ygorxkharo.obey.web.domain.recordlabels.model.RecordLabel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DefaultRecordLabelResolverTest {

    private val sonyRecordLabelName = "sony"
    private val sonyRecordLabelAvatar = "images/music_room/barscan/img_sony_records_avatar.png"
    private val universalRecordLabelName = "universal"
    private val universalLabelAvatar = "images/music_room/barscan/img_universal_records_avatar.png"
    private val warnerRecordLabelName = "warner"
    private val warnerLabelAvatar = "images/music_room/barscan/img_universal_records_avatar.png"

    private val recordLabelCollection = mapOf(
        sonyRecordLabelName to RecordLabel(name = sonyRecordLabelName, avatarImageUrl = sonyRecordLabelAvatar),
        "umg" to RecordLabel(name = universalRecordLabelName, avatarImageUrl = universalLabelAvatar),
        warnerRecordLabelName to RecordLabel(name = warnerRecordLabelName, avatarImageUrl = warnerLabelAvatar)
    )

    private val randomLabelCollection = listOf(
        RecordLabel(name = sonyRecordLabelName.plus("*"), avatarImageUrl = sonyRecordLabelAvatar),
        RecordLabel(name = universalRecordLabelName.plus("*"), avatarImageUrl = universalLabelAvatar),
        RecordLabel(name = warnerRecordLabelName.plus("*"), avatarImageUrl = warnerLabelAvatar)
    )

    private val sut = DefaultRecordLabelResolver(recordLabelCollection, randomLabelCollection)

    @Test
    fun test_When_CopyrightContainsUniversalKeyword_Expect_UniversalRecordLabelToBeReturned() {
        //Arrange
        val copyrightValue = "℗ 2022 The Weeknd XO, Inc., marketed by Republic Records, a division of UMG Recordings, Inc."

        //Act
        val actualRecordLabel = sut.resolveFromCopyright(copyrightValue)

        //Assert
        assertEquals(universalRecordLabelName, actualRecordLabel?.name)
        assertEquals(universalLabelAvatar, actualRecordLabel?.avatarImageUrl)
    }

    @Test
    fun test_When_CopyrightContainsSonyKeyword_Expect_SonyRecordLabelToBeReturned() {
        //Arrange
        val copyrightValue = "℗ 2022 Dizzy Records, Inc, a division of Sony Records, Inc."

        //Act
        val actualRecordLabel = sut.resolveFromCopyright(copyrightValue)

        //Assert
        assertEquals(sonyRecordLabelName, actualRecordLabel?.name)
        assertEquals(sonyRecordLabelAvatar, actualRecordLabel?.avatarImageUrl)
    }

    @Test
    fun test_When_CopyrightContainsWarnerKeyword_Expect_WarnerRecordLabelToBeReturned() {
        //Arrange
        val copyrightValue = "℗ 2022 Falco Records a division of Warner Recordings, Inc."

        //Act
        val actualRecordLabel = sut.resolveFromCopyright(copyrightValue)

        //Assert
        assertEquals(warnerRecordLabelName, actualRecordLabel?.name)
        assertEquals(warnerLabelAvatar, actualRecordLabel?.avatarImageUrl)
    }

    @Test
    fun test_When_CopyrightContainsNoMajorLabelKeyword_Expect_RecordLabelWithAsteriskToBeReturned() {
        //Arrange
        val copyrightValue = "℗ 2022 Falco Records a division of Falco Holdings Recordings, Inc."

        //Act
        val actualRecordLabel = sut.resolveFromCopyright(copyrightValue)

        //Assert
        assertTrue(actualRecordLabel?.name?.contains("*") == true)
    }
}