package com.madeean.storyapp2.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.madeean.storyapp2.data.response.DetailStoryResponseModel
import com.madeean.storyapp2.databinding.ItemStoryBinding
import com.madeean.storyapp2.ui.home.adapter.listener.StoryListListener

class StoryListAdapter : ListAdapter<DetailStoryResponseModel, ViewHolder>(StoryListCallBack()) {

  private lateinit var onItemCallBackListener: StoryListListener
  fun setOnItemClickCallback(onItemClickCallback: StoryListListener) {
    this.onItemCallBackListener = onItemClickCallback
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return StoryListViewHolder(
      ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false),
      StoryListAdapter()
    )
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val vaultItemHolder = holder as StoryListViewHolder
    val data = getItem(position)
    vaultItemHolder.bind(data, onItemCallBackListener)
  }

}