package com.madeean.storyapp2.data.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class AllStoryResponseModel(
  val error: Boolean,
  val message: String,
  val listStory: ArrayList<DetailStoryResponseModel>
)

@Parcelize
data class DetailStoryResponseModel(
  val id: String,
  val name: String,
  val description: String,
  val photoUrl: String,
  val createdAt: String,
  val lat: Float,
  val long: Float
) : Parcelable