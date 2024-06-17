package com.madeean.storyapp2.data.retrofit

import com.madeean.storyapp2.data.response.AllStoryResponseModel
import com.madeean.storyapp2.data.response.LoginResponseModel
import com.madeean.storyapp2.data.response.RegisterResponseModel
import com.madeean.storyapp2.ui.authentication.LoginPostModel
import com.madeean.storyapp2.ui.authentication.RegisterPostModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
  @POST("register")
  suspend fun register(
    @Body registerPostModel: RegisterPostModel
  ): Response<RegisterResponseModel>

  @POST("login")
  suspend fun login(
    @Body loginPostModel: LoginPostModel
  ): Response<LoginResponseModel>

  @GET("stories")
  suspend fun getAllStory(
    @Header("Authorization") token: String
  ): Response<AllStoryResponseModel>

  @Multipart
  @POST("stories")
  suspend fun uploadStory(
    @Part photo: MultipartBody.Part,
    @Part("description") description: RequestBody,
    @Header("Authorization") token: String,
  ): Response<RegisterResponseModel>
}