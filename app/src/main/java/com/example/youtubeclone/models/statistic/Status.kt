package com.example.youtubeclone.models.statistic

data class Status(
    val embeddable: Boolean,
    val license: String,
    val madeForKids: Boolean,
    val privacyStatus: String,
    val publicStatsViewable: Boolean,
    val uploadStatus: String
)