package com.madeean.storyapp2.ui.authentication

data class RegisterPostModel(
  val name: String,
  val email: String,
  val password: String
)

data class LoginPostModel(
  val email: String,
  val password: String
)