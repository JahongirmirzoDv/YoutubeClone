package com.example.youtubeclone.models.channel

data class Channel(
    val country: String,
    val description: String,
    val keywords: String,
    val moderateComments: Boolean,
    val title: String,
    val trackingAnalyticsAccountId: String,
    val unsubscribedTrailer: String
)