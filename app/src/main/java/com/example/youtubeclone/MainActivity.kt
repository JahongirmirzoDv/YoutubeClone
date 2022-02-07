package com.example.youtubeclone

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.youtubeclone.adapters.YoutubePagingRvAdapter
import com.example.youtubeclone.databinding.ActivityMainBinding
import com.example.youtubeclone.models.Item
import com.example.youtubeclone.models.YoutubeData
import com.example.youtubeclone.models.all_to_one.AllToOne
import com.example.youtubeclone.retrofit.ApiClient
import com.example.youtubeclone.retrofit.ApiHelperImpl
import com.example.youtubeclone.utils.PaginationScrollListener
import com.example.youtubeclone.utils.ViewModelFactory
import com.example.youtubeclone.viewmodels.ApiControlViewmodel
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: ApiControlViewmodel
    lateinit var alltoone: ArrayList<AllToOne>
    lateinit var paginationAdapter: YoutubePagingRvAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    private val TAG = "MainActivity"


    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private var TOTAL_PAGES = 10

    var a2 = ArrayList<com.example.youtubeclone.models.statistic.Item>()
    var a3 = listOf<com.example.youtubeclone.models.channel.Item>()

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        alltoone = ArrayList()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsCompat.toWindowInsetsCompat(WindowInsets.CONSUMED)
        }
        setupViewmodel()
        linearLayoutManager = LinearLayoutManager(this@MainActivity)
        paginationAdapter = YoutubePagingRvAdapter(this@MainActivity)



        GlobalScope.launch(Dispatchers.Main) {
            val loadData = loadData("")
//            val loadData2 = loadData2(loadData.items)
//            val loadData3 = loadData3(loadData2)

//            for (i in 0 until loadData.items.size) {
//                alltoone.add(
//                    AllToOne(
//                        loadData2[i].id,
//                        loadData.nextPageToken,
//                        loadData2[i].snippet.thumbnails.high.url,
//                        loadData2[i].snippet.title,
//                        loadData3[i].brandingSettings.channel.title,
//                        loadData2[i].statistics.viewCount,
//                        loadData2[i].snippet.publishedAt,
//                        loadData3[i].snippet.thumbnails.default.url,
//                        loadData3[i].id
//                    )
//                )
//            }
        }

        binding.mainRv.addOnScrollListener(object :
            PaginationScrollListener(linearLayoutManager) {
            override fun loadMoreItems() {
                isLoading = true
                currentPage++

//                loadNextPage()
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }
        })
        binding.mainRv.layoutManager = linearLayoutManager
        binding.mainRv.adapter = paginationAdapter
//        loadFirstPage()
    }

    @OptIn(DelicateCoroutinesApi::class)
//    private fun loadFirstPage() {
//        GlobalScope.launch(Dispatchers.Main) {
//            val loadData = loadData("")
//            val loadData2 = loadData2(loadData.items)
//            val loadData3 = loadData3(loadData2)
//
//            for (i in 0 until loadData.items.size) {
//                alltoone.add(
//                    AllToOne(
//                        loadData2[i].id,
//                        loadData.nextPageToken,
//                        loadData2[i].snippet.thumbnails.high.url,
//                        loadData2[i].snippet.title,
//                        loadData3[i].brandingSettings.channel.title,
//                        loadData2[i].statistics.viewCount,
//                        loadData2[i].snippet.publishedAt,
//                        loadData3[i].snippet.thumbnails.default.url,
//                        loadData3[i].id
//                    )
//                )
//            }
//            paginationAdapter.addAll(alltoone)
//        }
//
//        if (currentPage <= TOTAL_PAGES) {
//            paginationAdapter.editLoading()
//        } else {
//            isLastPage = true
//        }
//    }

//    fun loadNextPage() {
//        paginationAdapter.removeLoading()
//        isLoading = false
//        GlobalScope.launch(Dispatchers.Main) {
//            val loadData = loadData(alltoone[0].pageToken.toString())
//            val loadData2 = loadData2(loadData.items)
//            val loadData3 = loadData3(loadData2)
//
//            for (i in 0 until loadData.items.size) {
//                alltoone.add(
//                    AllToOne(
//                        loadData2[i].id,
//                        loadData.nextPageToken,
//                        loadData2[i].snippet.thumbnails.high.url,
//                        loadData2[i].snippet.title,
//                        loadData3[i].brandingSettings.channel.title,
//                        loadData2[i].statistics.viewCount,
//                        loadData2[i].snippet.publishedAt,
//                        loadData3[i].snippet.thumbnails.default.url,
//                        loadData3[i].id
//                    )
//                )
//            }
//            paginationAdapter.addAll(alltoone)
//        }
//
//        paginationAdapter.addAll(alltoone)
//
//        if (currentPage <= TOTAL_PAGES) {
//            paginationAdapter.editLoading()
//        } else {
//            isLastPage = true
//        }
//    }


    suspend fun loadData(pageToken: String) {
        withContext(Dispatchers.Main) {
            ApiClient.apiService.getRandom(pageToken = pageToken).items.map {
                a2.addAll(
                    ApiClient.apiService.getVideoDataById(
                        id = it.id.videoId,
                        key = "AIzaSyBdj_NiLtAOPPqniO2_K56QB4IAhAHrPec"
                    ).items
                )
            }
        }
        a3 = a2.flatMap {
            ApiClient.apiService.getChannelById(
                id = it.snippet.channelId,
                key = "AIzaSyBdj_NiLtAOPPqniO2_K56QB4IAhAHrPec"
            ).items
        }
        Log.e(TAG, "loadDatasize: ${a2.size}")
        Log.e(TAG, "loadDatasize: ${a3.size}")
    }

    suspend fun loadData2(n1: List<Item>): ArrayList<com.example.youtubeclone.models.statistic.Item> {
        withContext(Dispatchers.Main) {
            for (i in n1) {
                a2.addAll(
                    ApiClient.apiService.getVideoDataById(
                        id = i.id.videoId,
                        key = "AIzaSyBdj_NiLtAOPPqniO2_K56QB4IAhAHrPec"
                    ).items
                )
            }
        }
        return a2
    }

//    suspend fun loadData3(n1: List<com.example.youtubeclone.models.statistic.Item>): ArrayList<com.example.youtubeclone.models.channel.Item> {
//        withContext(Dispatchers.Main) {
//            for (i in n1) {
//                a3.addAll(
//                    ApiClient.apiService.getChannelById(
//                        id = i.snippet.channelId,
//                        key = "AIzaSyBdj_NiLtAOPPqniO2_K56QB4IAhAHrPec"
//                    ).items
//                )
//            }
//        }
//        return a3
//    }

    private fun setupViewmodel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                ApiHelperImpl(ApiClient.apiService)
            )
        )[ApiControlViewmodel::class.java]
    }

//    for (k in a2!!) {
//        viewModel.getChannelById(k.id).observe(this@MainActivity) {
//            when (it.status) {
//                Status.SUCCESS -> {
//                    Log.d(TAG, "succes: ${it.data}")
//                    a3 = it.data?.items
//                    for (m in 0 until a1!!.size) {
//                        alltoone.add(
//                            AllToOne(
//                                a2!![m].id,
//                                a2!![m].snippet.thumbnails.high.url,
//                                a2!![m].snippet.title,
//                                a3!![m].brandingSettings.channel.title,
//                                a2!![m].statistics.viewCount,
//                                a2!![m].snippet.publishedAt,
//                                a3!![m].snippet.thumbnails.default.url,
//                                a3!![m].id
//                            )
//                        )
//                    }
//                }
//                Status.LOADING -> {
//                    Log.d(TAG, "loadData: loading...")
//                }
//                Status.ERROR -> {
//                    Log.d(TAG, "error: ${it.message}")
//                }
//            }
//        }
//    }
}