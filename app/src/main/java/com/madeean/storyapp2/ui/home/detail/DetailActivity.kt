package com.madeean.storyapp2.ui.home.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.madeean.storyapp2.R
import com.madeean.storyapp2.Utils.loadUseGifThumb
import com.madeean.storyapp2.Utils.parcelable
import com.madeean.storyapp2.data.response.DetailStoryResponseModel
import com.madeean.storyapp2.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
  private lateinit var binding: ActivityDetailBinding
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityDetailBinding.inflate(layoutInflater)
    setContentView(binding.root)

    loadData()
    setupButton()
  }

  private fun setupButton() {
    binding.ivBack.setOnClickListener {
      finish()
    }
  }

  private fun loadData() {
    val data = intent.parcelable<DetailStoryResponseModel>(EXTRA_DATA_DETAIL) ?: return
    setupView(data)
  }

  private fun setupView(data: DetailStoryResponseModel) {
    binding.run {
      tvTitleDetailStory.text = data.name
      tvDescriptionDetailStory.text = data.description
      ivDetailStory.loadUseGifThumb(imageUrl = data.photoUrl, resGif = R.drawable.animation_loading)
    }
  }


  companion object {
    const val EXTRA_DATA_DETAIL = "extra_data_detail"

    fun newIntent(context: Context, data: DetailStoryResponseModel): Intent {
      val intent = Intent(context, DetailActivity::class.java)
      intent.putExtra(EXTRA_DATA_DETAIL, data)
      return intent
    }
  }
}