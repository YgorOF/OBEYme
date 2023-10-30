package com.ygorxkharo.obey.web.data.radiostaions.sdk.applemusic.dto

/**
 * Test fixture to provide Apple Music radio stations payload data
 */
object AppleMusicRadioStationTestFixture {

    val RADIO_STATION_DTO_PAYLOAD = """
        {
            "id": "ra.u-c431007bd49dd9891abb70ff757aa95f",
            "type": "stations",
            "href": "/v1/catalog/ng/stations/ra.u-c431007bd49dd9891abb70ff757aa95f",
            "attributes": {
                "artwork": {
                    "width": 2400,
                    "height": 2400,
                    "url": "https://is1-ssl.mzstatic.com/image/thumb/Features124/v4/7b/1d/f0/7b1df048-0017-8ac0-98c9-735f14849606/mza_7507996640781423701.png/{w}x{h}bb.jpeg"
                },
                "isLive": false,
                "playParams": {
                    "id": "ra.u-c431007bd49dd9891abb70ff757aa95f",
                    "kind": "radioStation",
                    "format": "tracks",
                    "stationHash": "CgoIByIGCPnAoplLEAE",
                    "mediaType": 0
                },
                "url": "https://music.apple.com/ng/station/roberts-station/ra.u-c431007bd49dd9891abb70ff757aa95f",
                "name": "Robert's Station",
                "mediaKind": "audio"
            }
        }
    """.trimIndent()

    val RADIO_STATION_COLLECTION_DTO_PAYLOAD = """
        {
        "data": [
            $RADIO_STATION_DTO_PAYLOAD
        ],
        "meta": {
            "filters": {
                "identity": {
                    "personal": [
                        {
                            "id": "ra.u-c431007bd49dd9891abb70ff757aa95f",
                            "type": "stations",
                            "href": "/v1/catalog/ng/stations/ra.u-c431007bd49dd9891abb70ff757aa95f"
                        }
                    ]
                }
            }
        }
    }
    """.trimIndent()

}