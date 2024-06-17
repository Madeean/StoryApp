package com.madeean.storyapp2.ui.home.adapter

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.madeean.storyapp2.R
import com.madeean.storyapp2.Utils.loadUseGifThumb
import com.madeean.storyapp2.data.response.DetailStoryResponseModel
import com.madeean.storyapp2.databinding.ItemStoryBinding
import com.madeean.storyapp2.ui.home.adapter.listener.StoryListListener

class StoryListViewHolder(private val binding: ItemStoryBinding, val adapter: StoryListAdapter) :
  ViewHolder(binding.root) {

  fun bind(
    data: DetailStoryResponseModel,
    onItemCallBackListener: StoryListListener
  ) {
    binding.run {
      tvTitle.text = data.name
      tvDescription.text = data.description
      ivStory.loadUseGifThumb(imageUrl = data.photoUrl, resGif = R.drawable.animation_loading)
      root.setOnClickListener {
        onItemCallBackListener.onItemClickListener(
          data,
          tvTitle,
          tvDescription,
          ivStory,
          it.context
        )
      }
    }
  }
}