package com.madeean.storyapp2.data.repository

import com.madeean.storyapp2.Utils.TERJADI_KESALAHAN_PADA_SERVER
import com.madeean.storyapp2.data.response.AllStoryResponseModel
import com.madeean.storyapp2.data.response.RegisterResponseModel
import com.madeean.storyapp2.data.retrofit.ApiService
import com.madeean.storyapp2.ui.home.model.UploadStoryPostModel
import org.json.JSONObject

class StoryRepository private constructor(
  private val apiService: ApiService,
) {

  suspend fun getAllStory(token: String): AllStoryResponseModel {
    val rawResponse = apiService.getAllStory(token)

    return if (rawResponse.isSuccessful) {
      val response = rawResponse.body() ?: throw Exception(TERJADI_KESALAHAN_PADA_SERVER)
      if (!response.error) {
        response
      } else {
        AllStoryResponseModel(
          error = true,
          message = response.message,
          listStory = response.listStory
        )
      }
    } else {
      AllStoryResponseModel(
        error = true,
        message = try {
          val errorBody = rawResponse.errorBody()?.string()
          JSONObject(errorBody ?: "").getString("message")
        } catch (e: Exception) {
          TERJADI_KESALAHAN_PADA_SERVER
        },
        listStory = arrayListOf()
      )
    }
  }

  suspend fun uploadStory(token: String, data: UploadStoryPostModel): RegisterResponseModel {
    val rawResponse = apiService.uploadStory(data.photo, data.description, token)

    return if (rawResponse.isSuccessful) {
      val response = rawResponse.body() ?: throw Exception(TERJADI_KESALAHAN_PADA_SERVER)
      if (!response.error) {
        response
      } else {
        RegisterResponseModel(
          error = true,
          message = response.message
        )
      }
    } else {
      RegisterResponseModel(
        error = true,
        message = try {
          val errorBody = rawResponse.errorBody()?.string()
          JSONObject(errorBody ?: "").getString("message")
        } catch (e: Exception) {
          TERJADI_KESALAHAN_PADA_SERVER
        }
      )
    }
  }

  companion object {
    @Volatile
    private var instance: StoryRepository? = null
    fun getInstance(
      apiService: ApiService,
    ): StoryRepository =
      instance ?: synchronized(this) {
        instance ?: StoryRepository(apiService)
      }.also { instance = it }
  }
}