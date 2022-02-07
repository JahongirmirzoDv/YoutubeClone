package com.example.youtubeclone.models.statistic

data class StatisticItem(
    val etag: String,
    val items: List<Item>,
    val kind: String,
    val pageInfo: PageInfo
)