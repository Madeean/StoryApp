package com.madeean.storyapp2.ui.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.madeean.storyapp2.data.repository.AuthenticationRepository
import com.madeean.storyapp2.data.response.LoginResponseModel
import com.madeean.storyapp2.data.response.RegisterResponseModel
import com.madeean.storyapp2.ui.preference.AuthenticationPreferences
import kotlinx.coroutines.launch

class AuthenticationViewModel(
  private val authenticationRepository: AuthenticationRepository,
  private val pref: AuthenticationPreferences
) : ViewModel() {

  private val _registerStatus = MutableLiveData<RegisterState>(RegisterState.Idle)
  val registerStatus: LiveData<RegisterState> = _registerStatus

  private val _loginStatus = MutableLiveData<LoginState>(LoginState.Idle)
  val loginStatus: LiveData<LoginState> = _loginStatus

  fun register(registerPostModel: RegisterPostModel) {
    viewModelScope.launch {
      _registerStatus.postValue(RegisterState.Loading)
      val response = authenticationRepository.register(registerPostModel)
      if (response.error) {
        _registerStatus.postValue(RegisterState.Error(response))
      } else {
        _registerStatus.postValue(RegisterState.Success(response))
      }
    }
  }

  fun login(loginPostModel: LoginPostModel) {
    viewModelScope.launch {
      _loginStatus.postValue(LoginState.Loading)
      val response = authenticationRepository.login(loginPostModel)
      if (response.error) {
        _loginStatus.postValue(LoginState.Error(response))
      } else {
        _loginStatus.postValue(LoginState.Success(response))
      }
    }
  }

  fun prefSaveToken(token: String) {
    viewModelScope.launch {
      pref.saveDataToken(token)
    }
  }

  fun getPrefToken(): LiveData<String> {
    return pref.getDataToken().asLiveData()
  }

  sealed interface RegisterState {
    data class Success(val data: RegisterResponseModel) : RegisterState
    data class Error(val data: RegisterResponseModel) : RegisterState
    data object Loading : RegisterState
    data object Idle : RegisterState
  }

  sealed interface LoginState {
    data class Success(val data: LoginResponseModel) : LoginState
    data class Error(val data: LoginResponseModel) : LoginState
    data object Loading : LoginState
    data object Idle : LoginState
  }
}
