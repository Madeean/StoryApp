package com.madeean.storyapp2.di

import android.content.Context
import com.madeean.storyapp2.data.repository.AuthenticationRepository
import com.madeean.storyapp2.data.repository.StoryRepository
import com.madeean.storyapp2.data.retrofit.ApiConfig

object Injection {
  fun authenticationProvideRepository(context: Context): AuthenticationRepository {
    val apiService = ApiConfig.getApiService(context)
    return AuthenticationRepository.getInstance(apiService)
  }

  fun storyProvideRepository(context: Context): StoryRepository {
    val apiService = ApiConfig.getApiService(context)
    return StoryRepository.getInstance(apiService)
  }
}