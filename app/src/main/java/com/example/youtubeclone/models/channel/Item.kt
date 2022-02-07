package com.example.youtubeclone.models.channel

data class Item(
    val brandingSettings: BrandingSettings,
    val contentDetails: ContentDetails,
    val etag: String,
    val id: String,
    val kind: String,
    val snippet: Snippet,
    val statistics: Statistics,
    val status: Status
)