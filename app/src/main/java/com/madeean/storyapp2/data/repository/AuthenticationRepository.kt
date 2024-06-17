package com.madeean.storyapp2.data.repository

import com.madeean.storyapp2.Utils.TERJADI_KESALAHAN_PADA_SERVER
import com.madeean.storyapp2.data.response.LoginResponseModel
import com.madeean.storyapp2.data.response.LoginResultResponseModel
import com.madeean.storyapp2.data.response.RegisterResponseModel
import com.madeean.storyapp2.data.retrofit.ApiService
import com.madeean.storyapp2.ui.authentication.LoginPostModel
import com.madeean.storyapp2.ui.authentication.RegisterPostModel
import org.json.JSONObject

class AuthenticationRepository private constructor(
  private val apiService: ApiService,
) {

  suspend fun register(data: RegisterPostModel): RegisterResponseModel {
    val response = apiService.register(data)

    return if (response.isSuccessful) {
      val registerResponse = response.body()
      if (registerResponse != null && !registerResponse.error) {
        registerResponse
      } else {
        RegisterResponseModel(
          error = true,
          message = registerResponse?.message ?: TERJADI_KESALAHAN_PADA_SERVER
        )
      }
    } else {
      RegisterResponseModel(
        error = true,
        message = try {
          val errorBody = response.errorBody()?.string()
          JSONObject(errorBody ?: "").getString("message")
        } catch (e: Exception) {
          TERJADI_KESALAHAN_PADA_SERVER
        }
      )
    }
  }

  suspend fun login(data: LoginPostModel): LoginResponseModel {
    val response = apiService.login(data)

    return if (response.isSuccessful) {
      val loginResponse = response.body() ?: throw Exception(TERJADI_KESALAHAN_PADA_SERVER)
      if (!loginResponse.error) {
        loginResponse
      } else {
        LoginResponseModel(
          error = true,
          message = loginResponse.message,
          loginResult = loginResponse.loginResult
        )
      }
    } else {
      LoginResponseModel(
        error = true,
        message = try {
          val errorBody = response.errorBody()?.string()
          JSONObject(errorBody ?: "").getString("message")
        } catch (e: Exception) {
          TERJADI_KESALAHAN_PADA_SERVER
        },
        loginResult = LoginResultResponseModel(
          userId = "",
          name = "",
          token = ""
        )
      )
    }
  }

  companion object {
    @Volatile
    private var instance: AuthenticationRepository? = null
    fun getInstance(
      apiService: ApiService,
    ): AuthenticationRepository =
      instance ?: synchronized(this) {
        instance ?: AuthenticationRepository(apiService)
      }.also { instance = it }
  }
}