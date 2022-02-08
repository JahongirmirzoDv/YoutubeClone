package com.example.youtubeclone.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.youtubeclone.models.YoutubeData
import com.example.youtubeclone.models.all_to_one.AllToOne
import com.example.youtubeclone.models.channel.ChannelStatistics
import com.example.youtubeclone.models.statistic.Item
import com.example.youtubeclone.models.statistic.StatisticItem
import com.example.youtubeclone.retrofit.ApiClient
import com.example.youtubeclone.retrofit.ApiHelper
import com.example.youtubeclone.utils.Resource
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class ApiControlViewmodel(var apiHelper: ApiHelper) : ViewModel() {
    var randomVideo = listOf<com.example.youtubeclone.models.Item>()
    var videoStat = arrayListOf<Item>()
    var channelStat = arrayListOf<com.example.youtubeclone.models.channel.Item>()
    var videoById = MutableLiveData<StatisticItem>()
    var channelById = MutableLiveData<ChannelStatistics>()
    var pageToken = ""
    private val TAG = "ApiControlViewmodel"


    @OptIn(FlowPreview::class)
    @JvmName("getRandomVideo1")
    fun getRandomVideo(): ArrayList<AllToOne> {
        var alltoone = ArrayList<AllToOne>()
        var all = AllToOne()
        viewModelScope.launch {
            coroutineScope {
                supervisorScope {
                    try {
                        apiHelper.getRandom("").flatMapConcat {
                            pageToken = it.nextPageToken
                            it.items.collect {
                                it.forEach {
                                    apiHelper.getChannelById(it.id.videoId).map {
                                        channelStat.addAll(it.items)
                                        Log.e(TAG, "getRandomVideo: ${it.items.size}")
                                    }.flowOn(coroutineContext)
                                }
                            }
                        }.flowOn(coroutineContext)
                        for (i in randomVideo.indices) {
                            alltoone.add(
                                AllToOne(
                                    videoStat[i].id,
                                    pageToken,
                                    videoStat[i].snippet.thumbnails.high.url,
                                    videoStat[i].snippet.title,
                                    channelStat[i].brandingSettings.channel.title,
                                    videoStat[i].statistics.viewCount,
                                    videoStat[i].snippet.publishedAt,
                                    channelStat[i].snippet.thumbnails.default.url,
                                    channelStat[i].id
                                )
                            )
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
        Log.e(TAG, "pagetoken: $pageToken")
        return alltoone
    }
}