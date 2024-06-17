package com.madeean.storyapp2.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.madeean.storyapp2.data.repository.AuthenticationRepository
import com.madeean.storyapp2.data.repository.StoryRepository
import com.madeean.storyapp2.di.Injection
import com.madeean.storyapp2.ui.authentication.AuthenticationViewModel
import com.madeean.storyapp2.ui.home.HomeViewModel
import com.madeean.storyapp2.ui.preference.AuthenticationPreferences

class ViewModelFactory constructor(
  private val authenticationRepository: AuthenticationRepository? = null,
  private val storyRepository: StoryRepository? = null,
  private val pref: AuthenticationPreferences? = null
) : ViewModelProvider.NewInstanceFactory() {
  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(AuthenticationViewModel::class.java)) {
      return AuthenticationViewModel(
        authenticationRepository
          ?: throw IllegalArgumentException("AuthenticationRepository is null"),
        pref ?: throw IllegalArgumentException("Preferences is null")
      ) as T
    } else if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
      return HomeViewModel(
        storyRepository
          ?: throw IllegalArgumentException("storyRepository is null"),
        pref ?: throw IllegalArgumentException("Preferences is null")
      ) as T
    }

    throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
  }

  companion object {
    @Volatile
    private var instance: ViewModelFactory? = null
    fun getInstance(
      context: Context,
      pref: AuthenticationPreferences? = null
    ): ViewModelFactory =
      instance ?: synchronized(this) {
        instance ?: ViewModelFactory(
          Injection.authenticationProvideRepository(context),
          Injection.storyProvideRepository(context),
          pref
        )
      }.also { instance = it }
  }
}