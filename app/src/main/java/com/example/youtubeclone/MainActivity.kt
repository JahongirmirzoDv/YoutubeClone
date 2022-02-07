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
import java.util.*
import kotlin.collections.ArrayList

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
    private var pageToken1 = ""


    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        alltoone = ArrayList()
        binding.progress.visibility = View.VISIBLE

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsCompat.toWindowInsetsCompat(WindowInsets.CONSUMED)
        }
        setupViewmodel()
        linearLayoutManager = LinearLayoutManager(this@MainActivity)
        paginationAdapter = YoutubePagingRvAdapter(this@MainActivity)


        binding.mainRv.addOnScrollListener(object :
            PaginationScrollListener(linearLayoutManager) {
            override fun loadMoreItems() {
                isLoading = true
                currentPage++

                loadNextPage()
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
        loadFirstPage()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun loadFirstPage() {
        var nextpagetoken = ""

        GlobalScope.launch(Dispatchers.Main) {
            var a2 = ArrayList<com.example.youtubeclone.models.statistic.Item>()
            var a3 = listOf<com.example.youtubeclone.models.channel.Item>()
            async {
                nextpagetoken = ApiClient.apiService.getRandom(pageToken = "").nextPageToken
            }
            ApiClient.apiService.getRandom(pageToken = "").items.map {
                a2.addAll(
                    ApiClient.apiService.getVideoDataById(
                        id = it.id.videoId,
                        key = "AIzaSyBdj_NiLtAOPPqniO2_K56QB4IAhAHrPec"
                    ).items
                )
            }

            a3 = a2.flatMap {
                ApiClient.apiService.getChannelById(
                    id = it.snippet.channelId,
                    key = "AIzaSyBdj_NiLtAOPPqniO2_K56QB4IAhAHrPec"
                ).items
            }
            Log.e(TAG, "1a2: ${a2.size}")
            Log.e(TAG, "1a3: ${a3.size}")

            for (i in 0 until a2.size) {
                alltoone.add(
                    AllToOne(
                        a2[i].id,
                        nextpagetoken,
                        a2[i].snippet.thumbnails.high.url,
                        a2[i].snippet.title,
                        a3[i].brandingSettings.channel.title,
                        a2[i].statistics.viewCount,
                        a2[i].snippet.publishedAt,
                        a3[i].snippet.thumbnails.default.url,
                        a3[i].id
                    )
                )
            }
        }
        paginationAdapter.addAll(alltoone)
        paginationAdapter.notifyDataSetChanged()
        binding.progress.visibility = View.INVISIBLE

        Log.e(TAG, "loadFirstPage: ${alltoone}")

        if (currentPage <= TOTAL_PAGES) {
            paginationAdapter.editLoading()
        } else {
            isLastPage = true
        }
    }

    fun loadNextPage() {
        paginationAdapter.removeLoading()
        isLoading = false
        var nextpagetoken = ""
        GlobalScope.launch(Dispatchers.Main) {
          async{
              var a2 = ArrayList<com.example.youtubeclone.models.statistic.Item>()
              var a3 = listOf<com.example.youtubeclone.models.channel.Item>()
              async {
                  nextpagetoken = ApiClient.apiService.getRandom(pageToken = "").nextPageToken
              }.await()
              ApiClient.apiService.getRandom(pageToken = nextpagetoken).items.map {
                  a2.addAll(
                      ApiClient.apiService.getVideoDataById(
                          id = it.id.videoId,
                          key = "AIzaSyBdj_NiLtAOPPqniO2_K56QB4IAhAHrPec"
                      ).items
                  )
              }

              a3 = a2.flatMap {
                  ApiClient.apiService.getChannelById(
                      id = it.snippet.channelId,
                      key = "AIzaSyBdj_NiLtAOPPqniO2_K56QB4IAhAHrPec"
                  ).items
              }
              Log.e(TAG, "a2: ${a2.size}")
              Log.e(TAG, "a3: ${a3.size}")

              for (i in 0 until a2.size) {
                  alltoone.add(
                      AllToOne(
                          a2[i].id,
                          nextpagetoken,
                          a2[i].snippet.thumbnails.high.url,
                          a2[i].snippet.title,
                          a3[i].brandingSettings.channel.title,
                          a2[i].statistics.viewCount,
                          a2[i].snippet.publishedAt,
                          a3[i].snippet.thumbnails.default.url,
                          a3[i].id
                      )
                  )
              }
          }.await()
            paginationAdapter.addAll(alltoone)
        }


        if (currentPage <= TOTAL_PAGES) {
            paginationAdapter.editLoading()
        } else {
            isLastPage = true
        }
    }

    private fun setupViewmodel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                ApiHelperImpl(ApiClient.apiService)
            )
        )[ApiControlViewmodel::class.java]
    }
}