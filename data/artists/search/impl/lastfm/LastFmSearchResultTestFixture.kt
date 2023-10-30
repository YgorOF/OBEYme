package com.ygorxkharo.obey.web.data.artists.search.impl.lastfm

object LastFmSearchResultTestFixture {

    val ARTIST_SEARCH_RESULT_RESPONSE = """
        {
            "results": {
                "opensearch:totalResults": "11365",
                "opensearch:startIndex": "0",
                "opensearch:itemsPerPage": "5",
                "artistmatches": {
                    "artist": [
                        {
                            "name": "Ed Sheeran"
                        },
                        {
                            "name": "Ed Sheeran & Justin Bieber"
                        },
                        {
                            "name": "Ed Sheeran & Passenger"
                        },
                        {
                            "name": "Ed Sheeran & Yelawolf"
                        },
                        {
                            "name": "ED SHEERAN AND JUSTIN BIEBER"
                        }
                    ]
                }
            }
        }
    """.trimIndent()
}