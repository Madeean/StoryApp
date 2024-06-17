package com.madeean.storyapp2.ui.home.adapter.listener

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.madeean.storyapp2.data.response.DetailStoryResponseModel


interface StoryListListener{
  fun onItemClickListener(
    data: DetailStoryResponseModel,
    tvTitle: TextView,
    tvDescription: TextView,
    ivStory: ImageView,
    context: Context
  )
}