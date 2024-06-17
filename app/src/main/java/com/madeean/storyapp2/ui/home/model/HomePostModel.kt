package com.madeean.storyapp2.ui.home.model

import okhttp3.MultipartBody
import okhttp3.RequestBody

data class UploadStoryPostModel(
  val description: RequestBody,
  val photo: MultipartBody.Part,
)