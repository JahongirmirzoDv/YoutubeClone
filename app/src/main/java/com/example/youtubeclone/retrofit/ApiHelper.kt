package com.example.youtubeclone.retrofit

import com.example.youtubeclone.models.YoutubeData
import com.example.youtubeclone.models.channel.ChannelStatistics
import com.example.youtubeclone.models.statistic.StatisticItem


interface ApiHelper {
    suspend fun getRandom(pageToken:String): YoutubeData

    suspend fun getVideoById(id: String): StatisticItem

    suspend fun getChannelById(id: String): ChannelStatistics
}