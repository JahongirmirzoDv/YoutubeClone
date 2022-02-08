package com.example.youtubeclone.retrofit

import com.example.youtubeclone.models.YoutubeData
import com.example.youtubeclone.models.channel.ChannelStatistics
import com.example.youtubeclone.models.statistic.StatisticItem
import kotlinx.coroutines.flow.Flow


class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {
    private var key = "AIzaSyBdj_NiLtAOPPqniO2_K56QB4IAhAHrPec"
    override suspend fun getRandom(pageToken: String): Flow<YoutubeData> =
        apiService.getRandom(pageToken = pageToken)

    override suspend fun getVideoById(id: String): Flow<StatisticItem> =
        apiService.getVideoDataById(id = id, key = key)

    override suspend fun getChannelById(id: String): Flow<ChannelStatistics> =
        apiService.getChannelById(id = id, key = key)


}