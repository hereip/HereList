package com.hereip.herelist.data

data class Track(
    val title: String,
    val artist: String,
    val album: String,
    val duration: String
)

val DemoTracks = listOf(
    Track("Night Drive", "HereList Studio", "Demo Collection", "3:42"),
    Track("Blue Horizon", "HereList Studio", "Demo Collection", "4:08"),
    Track("Afterglow", "HereList Studio", "Demo Collection", "3:16"),
    Track("Quiet City", "HereList Studio", "Demo Collection", "2:58")
)
