package com.example.youtubeclone.models.statistic

data class Item(
    val contentDetails: ContentDetails,
    val etag: String,
    val id: String,
    val kind: String,
    val localizations: Localizations,
    val snippet: Snippet,
    val statistics: Statistics,
    val status: Status
)