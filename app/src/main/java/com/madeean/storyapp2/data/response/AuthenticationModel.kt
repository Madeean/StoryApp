package com.madeean.storyapp2.data.response

data class RegisterResponseModel(
  val error: Boolean,
  val message: String
)

data class LoginResponseModel(
  val error: Boolean,
  val message: String,
  val loginResult: LoginResultResponseModel
)

data class LoginResultResponseModel(
  val userId: String,
  val name: String,
  val token: String
)
