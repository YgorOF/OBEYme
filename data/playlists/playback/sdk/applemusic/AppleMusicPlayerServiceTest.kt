package com.ygorxkharo.obey.web.data.playlists.playback.sdk.applemusic

import com.ygorxkharo.obey.web.data.playlists.integration.MusicPlatformLibraryException
import com.ygorxkharo.obey.web.data.playlists.playback.MusicPlayer
import com.ygorxkharo.obey.web.data.playlists.playback.MusicPlayerEventListener
import com.ygorxkharo.obey.web.data.playlists.playback.PlaybackEventListener
import com.ygorxkharo.obey.web.data.playlists.playback.RecordLabelResolver
import com.ygorxkharo.obey.web.data.playlists.playback.model.PlayBackStateEvent
import com.ygorxkharo.obey.web.data.playlists.playback.model.PlaybackItemAttributes
import com.ygorxkharo.obey.web.domain.musicplatforms.playlists.playback.model.MusicLibraryPlaybackRequest
import com.ygorxkharo.obey.web.domain.musicplatforms.playlists.playback.model.MusicPlaybackItem
import com.ygorxkharo.obey.web.domain.recordlabels.MusicPlatformAlbumService
import com.ygorxkharo.obey.web.domain.recordlabels.model.AlbumRequest
import com.ygorxkharo.obey.web.domain.recordlabels.model.AlbumResult
import com.ygorxkharo.obey.web.domain.recordlabels.model.RecordLabel
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AppleMusicPlayerServiceTest {

    private var isPlaybackEnded = false
    private val musicKitAccessToken = "AF.sljdhfakjhdf9ayf9ayshdfiohasfkljhdsfsdf"
    private lateinit var eventPayload: PlaybackItemAttributes
    private var isMidPointReachedCalled = false

    private val musicPlayerEventListener = object: MusicPlayerEventListener {
        override fun onPlaybackEnded() {
            isPlaybackEnded = true
        }

        override fun onPlayCurrentTrack(playbackItem: PlaybackItemAttributes) {
            eventPayload = playbackItem
        }

        override fun onSongMidPointReached() {
            isMidPointReachedCalled = true
        }
    }

    private val playbackRequest = MusicLibraryPlaybackRequest(
        musicPlatformName = "test",
        playbackItemType = MusicPlaybackItem.PLAYLIST,
        itemId = "p.xklwrwlkjafda"
    )

    @BeforeTest
    fun setup() {
        isMidPointReachedCalled = false
    }

    @Test
    fun test_When_MusicPlaybackEnds_Expect_ThePlaybackEndedToBeCalledOnThePlayerListener() {
        //Arrange
        val musicPlayer = stubMusicPlayer(PlayBackStateEvent.ENDED)
        val sut = AppleMusicPlayerService(musicPlayerEventListener, musicPlayer)

        //Act
        sut.playLibraryMedia(playbackRequest)

        //Assert
        assertTrue(isPlaybackEnded)
    }

    @Test
    fun test_When_PlayingEventIsTriggered_Expect_PlaybackAttributesToBeSentViaAppEventBus() {
        // Arrange
        val expectedMediaTitle = "test title"
        val expectedArtistName = "test artist"
        val expectedCoverImageUrl = "http://image.png"
        val expectedAlbumId = "1243434242"

        val expectedPlaybackItemAttributes = PlaybackItemAttributes(
            mediaTitle = expectedMediaTitle,
            artistName = expectedArtistName,
            coverArtImageUrl = expectedCoverImageUrl,
            albumId = expectedAlbumId
        )

        val copyrightDescription = "℗ 2022 Falco Records a division of Warner Recordings, Inc."
        val expectedRecordLabelName = "warner"
        val musicPlayer = stubMusicPlayer(PlayBackStateEvent.PLAYING)
        val recordLabelResolver = stubRecordLabelResolver(expectedRecordLabelName)
        val albumService = stubAlbumService(copyrightDescription = copyrightDescription)
        val sut = AppleMusicPlayerService(
            musicPlayerEventListener,
            musicPlayer,
            recordLabelResolver = recordLabelResolver,
            musicPlatformAlbumService = albumService,
            musicKitAccessToken = musicKitAccessToken
        )

        // Act
        sut.onPlaybackStateChanged(PlayBackStateEvent.PLAYING, expectedPlaybackItemAttributes)

        // Assert
        assertEquals(expectedMediaTitle, eventPayload.mediaTitle)
        assertEquals(expectedArtistName, eventPayload.artistName)
        assertEquals(expectedCoverImageUrl, eventPayload.coverArtImageUrl)
        assertEquals(expectedRecordLabelName, eventPayload.recordLabel?.name)
    }

    @Test
    fun test_When_PlaybackStateEventForSongMidPointIsTriggered_Expect_PlayerListenerToCallMidPointReached() {
        // Arrange
        val musicPlayer = stubMusicPlayer(PlayBackStateEvent.SONG_MID_POINT)
        val recordLabelResolver = stubRecordLabelResolver("")
        val albumService = stubAlbumService()
        val sut = AppleMusicPlayerService(
            musicPlayerEventListener,
            musicPlayer,
            recordLabelResolver = recordLabelResolver,
            musicPlatformAlbumService = albumService,
            musicKitAccessToken = musicKitAccessToken
        )

        // Act
        sut.onPlaybackStateChanged(PlayBackStateEvent.SONG_MID_POINT)

        // Assert
        assertTrue(isMidPointReachedCalled)
    }

    private fun stubMusicPlayer(playbackStateEvent: PlayBackStateEvent, playBackItem: PlaybackItemAttributes? = null): MusicPlayer<MusicLibraryPlaybackRequest> {
        return object: MusicPlayer<MusicLibraryPlaybackRequest> {
            override lateinit var playbackStateEventListener: PlaybackEventListener

            override fun playSong(playbackProperties: MusicLibraryPlaybackRequest) {
                playbackStateEventListener.onPlaybackStateChanged(playbackStateEvent, playBackItem)
            }

            override fun skipToTrackPosition(trackPosition: Int) {}
            override fun skipToNextTrack() {}
            override fun skipToPreviousTrack() {}
            override fun loadMusicLibrary(playbackProperties: MusicLibraryPlaybackRequest) {}
        }
    }

    private fun stubAlbumService(
        copyrightDescription: String? = null,
        errorMessage: String? = null
    ): MusicPlatformAlbumService {
        return object: MusicPlatformAlbumService {
            override fun getAlbumById(albumRequest: AlbumRequest, onComplete: (Result<AlbumResult>) -> Unit) {
                if(copyrightDescription != null) {
                    return onComplete.invoke(Result.success(AlbumResult(copyrightDescription)))
                }

                if(errorMessage != null) {
                    return onComplete.invoke(Result.failure(MusicPlatformLibraryException(errorMessage)))
                }
            }
        }
    }

    private fun stubRecordLabelResolver(recordLabelName: String): RecordLabelResolver {
        return object: RecordLabelResolver {
            override fun resolveFromCopyright(copyrightDescription: String): RecordLabel {
                return RecordLabel(recordLabelName, "")
            }
        }
    }
}