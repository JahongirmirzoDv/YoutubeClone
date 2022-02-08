package com.example.youtubeclone.models

import kotlinx.coroutines.flow.Flow

data class YoutubeData(
    val etag: String,
    val items: Flow<List<Item>>,
    val kind: String,
    val nextPageToken: String,
    val pageInfo: PageInfo,
    val regionCode: String
)