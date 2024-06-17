package com.madeean.storyapp2.ui.home.adapter

import androidx.recyclerview.widget.DiffUtil
import com.madeean.storyapp2.data.response.DetailStoryResponseModel

class StoryListCallBack : DiffUtil.ItemCallback<DetailStoryResponseModel>() {
  override fun areItemsTheSame(
    oldItem: DetailStoryResponseModel,
    newItem: DetailStoryResponseModel
  ): Boolean {
    return oldItem.hashCode() == newItem.hashCode()
  }

  override fun areContentsTheSame(
    oldItem: DetailStoryResponseModel,
    newItem: DetailStoryResponseModel
  ): Boolean {
    return oldItem.hashCode() == newItem.hashCode()
  }
}