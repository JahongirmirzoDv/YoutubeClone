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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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
       GlobalScope.launch(Dispatchers.Main) {
           val randomVideo = async { viewModel.getRandomVideo() }.await()
           paginationAdapter.addAll(randomVideo)
           binding.progress.visibility = View.INVISIBLE

           Log.e(TAG, "random: ${randomVideo}")
       }
        if (currentPage <= TOTAL_PAGES) {
            paginationAdapter.editLoading()
        } else {
            isLastPage = true
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun loadNextPage() {
        paginationAdapter.removeLoading()
        isLoading = false
        var nextpagetoken = ""
        val randomVideo = viewModel.getRandomVideo()
        paginationAdapter.addAll(randomVideo)

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