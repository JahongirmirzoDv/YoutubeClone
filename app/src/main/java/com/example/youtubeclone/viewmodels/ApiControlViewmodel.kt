package com.example.youtubeclone.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.youtubeclone.models.YoutubeData
import com.example.youtubeclone.models.channel.ChannelStatistics
import com.example.youtubeclone.models.statistic.StatisticItem
import com.example.youtubeclone.retrofit.ApiHelper
import com.example.youtubeclone.utils.Resource
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class ApiControlViewmodel(var apiHelper: ApiHelper) : ViewModel() {
    var randomVideo = MutableLiveData<YoutubeData>()
    var videoById = MutableLiveData<StatisticItem>()
    var channelById = MutableLiveData<ChannelStatistics>()


    @JvmName("getRandomVideo1")
    fun getRandomVideo(): MutableLiveData<YoutubeData> {
        viewModelScope.launch {
            coroutineScope {
                supervisorScope {
                    try {
                        val random = apiHelper.getRandom("")
                        randomVideo.value = random
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
        return randomVideo
    }

    fun getVideoById(id: String): MutableLiveData<StatisticItem> {
        viewModelScope.launch {
            coroutineScope {
                supervisorScope {
                    try {
                        val videoById1 = apiHelper.getVideoById(id)
                        videoById.value =videoById1
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
        return videoById
    }

    fun getChannelById(id: String): MutableLiveData<ChannelStatistics> {
        viewModelScope.launch {
            coroutineScope {
                supervisorScope {
                    try {
                        val channelById1 = apiHelper.getChannelById(id)
                        channelById.value = channelById1
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
        return channelById
    }
}