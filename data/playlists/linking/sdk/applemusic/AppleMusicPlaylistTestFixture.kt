package com.ygorxkharo.obey.web.data.playlists.linking.sdk.applemusic

/**
 * Provides testing fixtures for Apple Music playlist operations
 */
object AppleMusicPlaylistTestFixture {

    val PLAYLIST_JSON_PAYLOAD = """
        {
            "id": "p.oOzA3XOclW6AZ65",
            "attributes": {
                "artwork": {
                    "width": 1200,
                    "height": 1200,
                    "url": "https://example.com"
                },
                "name": "Army Drill Sounds",
                "canEdit": true,
                "dateAdded": "2021-06-30T09:35:21Z"
            }
        }
    """.trimIndent()

    val NON_EDITABLE_PLAYLIST_JSON_PAYLOAD = """
        {
            "id": "p.oOsfsfdxljq98n3w4",
            "attributes": {
                "artwork": {
                    "width": 1200,
                    "height": 1200,
                    "url": "https://non-editable-playlist.com"
                },
                "name": "Famous Starts",
                "canEdit": false,
                "dateAdded": "2021-06-30T09:35:21Z"
            }
        }
    """.trimIndent()

    val MIXED_PLAYLIST_JSON_PAYLOAD = """
        {
            "data": [
                $PLAYLIST_JSON_PAYLOAD,
                $NON_EDITABLE_PLAYLIST_JSON_PAYLOAD
            ],
            "meta" : {
                "total": 2
            }
        }
    """.trimIndent()

    val PLAYLIST_NO_COVER_ART_JSON_PAYLOAD = """
        {
            "id": "p.oOzA3XOclW6AZ65",
            "attributes": {
                "name": "Army Drill Sounds",
                "canEdit": true,
                "dateAdded": "2021-06-30T09:35:21Z"
            }
        }
    """.trimIndent()

    val PLAYLIST_COLLECTION_DTO_PAYLOAD = """
        {
            "data": [
                $PLAYLIST_JSON_PAYLOAD
            ],
            "meta": {
                "total": 1
            }
        }
    """.trimIndent()

    val ERROR_DTO_PAYLOAD_JSON_VALUE = """
        {
            "errors": [
                {
                    "id": "2WQFEK67IZORFNV7SNWBLMPE6A",
                    "title": "No related resources",
                    "detail": "No related resources found for tracks",
                    "status": "404",
                    "code": "40403"
                }
            ]
        }
    """.trimIndent()

    val RECOMMENDATIONS_DTO_STRING = """
        {
            "next": "/v1/me/recommendations?offset=1",
            "data": [
                {
                    "id": "6-27s5hU6azhJY",
                    "relationships": {
                        "contents": {
                            "href": "/v1/me/recommendations/6-27s5hU6azhJY/contents",
                            "data": [
                                {
                                    "id": "1444392995",
                                    "type": "albums",
                                    "attributes": {
                                        "artwork": {
                                            "width": 900,
                                            "height": 894,
                                            "url": "https://is3-ssl.mzstatic.com/image/thumb/Music118/v4/1e/c4/45/1ec4453c-fbd9-8d69-c7d9-6c5a4477c882/00602527336343.rgb.jpg/{w}x{h}bb.jpg"
                                        },
                                        "artistName": "Jay Sean",
                                        "genreNames": [
                                            "Hip-Hop/Rap",
                                            "Music"
                                        ],
                                        "releaseDate": "2010-01-01",
                                        "name": "Down (feat. Lil Wayne) - Single",
                                        "recordLabel": "Cash Money",
                                        "playParams": {
                                            "id": "1444392995",
                                            "kind": "album"
                                        }
                                    }
                                },
                                {
                                    "id": "1444392996",
                                    "type": "albums",
                                    "attributes": {
                                        "artwork": {
                                            "width": 900,
                                            "height": 894,
                                            "url": "https://is3-ssl.mzstatic.com/image/thumb/Music118/v4/1e/c4/45/1ec4453c-fbd9-8d69-c7d9-6c5a4477c882/00602527336343.rgb.jpg/{w}x{h}bb.jpg"
                                        },
                                        "artistName": "Rapid Fire",
                                        "genreNames": [
                                            "Classical",
                                            "Music"
                                        ],
                                        "releaseDate": "2010-01-01",
                                        "name": "Rapid Fire Crew",
                                        "recordLabel": "Cash Money",
                                        "playParams": {
                                            "id": "1444392996",
                                            "kind": "album"
                                        }
                                    }
                                }
                            ]
                        }
                    }
                }
            ]
        }
    """.trimIndent()
}