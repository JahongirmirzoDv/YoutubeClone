package com.example.youtubeclone.retrofit

import com.example.youtubeclone.models.YoutubeData
import com.example.youtubeclone.models.channel.ChannelStatistics
import com.example.youtubeclone.models.statistic.StatisticItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search")
    suspend fun getData(
        @Query("key") key: String = "AIzaSyBdj_NiLtAOPPqniO2_K56QB4IAhAHrPec",
        @Query("channelId") channelId: String = "UCLRXXDGv3L_gGxUHFY8D45g",
        @Query("part") part: String = "snippet,id",
        @Query("order") order: String = "date",
        @Query("maxResults") maxResults: Int = 20
    ): YoutubeData

    @GET("search")
    suspend fun getRandom(
        @Query("key") key: String = "AIzaSyBdj_NiLtAOPPqniO2_K56QB4IAhAHrPec",
        @Query("maxResults") maxResults: Int = 10,
        @Query("part") part: String = "snippet",
        @Query("type") type: String = "video",
        @Query("pageToken") pageToken: String,
    ): YoutubeData

    @GET("videos")
    suspend fun getVideoDataById(
        @Query("id") id: String,
        @Query("key") key: String,
        @Query("part") part: String = "snippet,contentDetails,statistics,status,localizations"
    ): StatisticItem

    @GET("channels")
    suspend fun getChannelById(
        @Query("id") id: String,
        @Query("key") key: String,
        @Query("part") part: String = "snippet,contentDetails,statistics,status,brandingSettings"
    ): ChannelStatistics
}