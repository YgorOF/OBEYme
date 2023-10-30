package com.ygorxkharo.obey.web.data.songs.linking.sdk.applemusic

/**
 * Provides test data for the Apple Music library song resource
 */
object AppleMusicLibrarySongTestFixture {

    const val APPLE_MUSIC_LIBRARY_SONG_JSON_RESPONSE = """
        {
            "next": "/v1/me/library/songs?offset=2",
            "data": [
                {
                    "id": "i.EYVbDJbumNdr2Q2",
                    "type": "library-songs",
                    "href": "/v1/me/library/songs/i.EYVbDJbumNdr2Q2",
                    "attributes": {
                        "artwork": {
                            "width": 1200,
                            "height": 1200,
                            "url": "https://is1-ssl.mzstatic.com/image/thumb/Music124/v4/0b/39/94/0b39941c-daf3-efc6-a778-e025a3ecbab1/5060438970110.jpg/{w}x{h}bb.jpeg"
                        },
                        "artistName": "Yemi Alade",
                        "discNumber": 1,
                        "genreNames": [
                            "Afro-Pop"
                        ],
                        "durationInMillis": 236827,
                        "releaseDate": "2016-03-25",
                        "name": "Africa (feat. Sauti Sol)",
                        "hasLyrics": true,
                        "albumName": "Mama Africa (The Diary of an African Woman)",
                        "playParams": {
                            "id": "i.EYVbDJbumNdr2Q2",
                            "kind": "song",
                            "isLibrary": true,
                            "reporting": true,
                            "catalogId": "1088796689"
                        },
                        "trackNumber": 7
                    }
                }
            ],
            "meta": {
                "total": 1
            }
        }
    """

}