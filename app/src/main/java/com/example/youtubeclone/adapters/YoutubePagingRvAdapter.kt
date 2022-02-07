package com.example.youtubeclone.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.youtubeclone.databinding.LoadItemBinding
import com.example.youtubeclone.databinding.PagingRvItemBinding
import com.example.youtubeclone.models.all_to_one.AllToOne

class YoutubePagingRvAdapter(var context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val LOADING = 0
    private val DATA = 1
    private var isLoadingAdded = false

    private val list = ArrayList<AllToOne>()
    lateinit var onpress: onPress


    inner class LoadingVh(var itemLoadBinding: LoadItemBinding) :
        RecyclerView.ViewHolder(itemLoadBinding.root) {

        fun onBind() {
            itemLoadBinding.progress.visibility = View.VISIBLE
        }
    }

    inner class DataVh(var itemDataBinding: PagingRvItemBinding) :
        RecyclerView.ViewHolder(itemDataBinding.root) {

        fun onBind(data: AllToOne) {
            itemDataBinding.apply {
                Glide.with(context).load(data.video_image).into(itemDataBinding.image)
                Glide.with(context).load(data.channel_image).into(itemDataBinding.channelImage)
                itemDataBinding.channelName.text = data.channel_name
                itemDataBinding.videoName.text = data.video_name
                itemDataBinding.videoViews.text = data.video_views
                itemDataBinding.videoDate.text = data.video_date

            }
            itemDataBinding.container.setOnClickListener {
                onpress.onclick(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == LOADING) {
            return LoadingVh(
                LoadItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            return DataVh(
                PagingRvItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemViewType = getItemViewType(position)
        if (itemViewType == LOADING) {
            val loadingVh = holder as LoadingVh
            loadingVh.onBind()
        } else {
            val dataVh = holder as DataVh
            dataVh.onBind(list[position])
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        return if (position == list.size - 1 && isLoadingAdded) LOADING else DATA
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addAll(otherList: List<AllToOne>) {
        list.addAll(otherList)
        notifyDataSetChanged()
    }

    fun editLoading() {
        isLoadingAdded = true
    }

    fun removeLoading() {
        isLoadingAdded = false
    }

    interface onPress {
        fun onclick(item: AllToOne)
    }
}