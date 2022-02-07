package com.example.youtubeclone.models.statistic

data class ContentDetails(
    val caption: String,
    val contentRating: ContentRating,
    val definition: String,
    val dimension: String,
    val duration: String,
    val licensedContent: Boolean,
    val projection: String
)