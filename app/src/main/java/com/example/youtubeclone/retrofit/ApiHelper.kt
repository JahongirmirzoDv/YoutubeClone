package com.example.youtubeclone.retrofit

import com.example.youtubeclone.models.YoutubeData
import com.example.youtubeclone.models.channel.ChannelStatistics
import com.example.youtubeclone.models.statistic.StatisticItem
import kotlinx.coroutines.flow.Flow


interface ApiHelper {
    suspend fun getRandom(pageToken:String): Flow<YoutubeData>

    suspend fun getVideoById(id: String): Flow<StatisticItem>

    suspend fun getChannelById(id: String): Flow<ChannelStatistics>
}