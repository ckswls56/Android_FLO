package com.example.flo

import kotlin.collections.ArrayList


data class Album(
        var title: String? = "",
        var singer: String? = "",
        var coverImg: Int? = null,
        var songs: ArrayList<Song>? = null
)
