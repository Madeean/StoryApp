package com.madeean.storyapp2.data.retrofit

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.madeean.storyapp2.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
  companion object {
    fun getApiService(context: Context): ApiService {
      val loggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
      val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(ChuckerInterceptor(context))
        .build()
      val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
      return retrofit.create(ApiService::class.java)
    }
  }
}