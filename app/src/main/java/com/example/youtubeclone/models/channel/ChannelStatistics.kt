package com.example.youtubeclone.models.channel

data class ChannelStatistics(
    val etag: String,
    val items: List<Item>,
    val kind: String,
    val pageInfo: PageInfo
)