package com.example.youtubeclone.models.channel

data class Status(
    val isLinked: Boolean,
    val longUploadsStatus: String,
    val madeForKids: Boolean,
    val privacyStatus: String
)