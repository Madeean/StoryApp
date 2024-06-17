package com.madeean.storyapp2.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.madeean.storyapp2.data.repository.StoryRepository
import com.madeean.storyapp2.data.response.AllStoryResponseModel
import com.madeean.storyapp2.data.response.RegisterResponseModel
import com.madeean.storyapp2.ui.home.model.UploadStoryPostModel
import com.madeean.storyapp2.ui.preference.AuthenticationPreferences
import kotlinx.coroutines.launch

class HomeViewModel(
  private val storyRepository: StoryRepository,
  private val pref: AuthenticationPreferences
) : ViewModel() {

  fun getPrefToken(): LiveData<String> {
    return pref.getDataToken().asLiveData()
  }

  fun logout() {
    viewModelScope.launch {
      pref.clearToken()
    }
  }

  private val _allStoryStatus = MutableLiveData<AllStoryState>(AllStoryState.Idle)
  val allStoryStatus: LiveData<AllStoryState> = _allStoryStatus

  private val _uploadStoryStatus = MutableLiveData<UploadStoryState>(UploadStoryState.Idle)
  val uploadStoryStatus: LiveData<UploadStoryState> = _uploadStoryStatus

  fun getAllStory(token: String) {
    viewModelScope.launch {
      _allStoryStatus.postValue(AllStoryState.Loading)
      val response = storyRepository.getAllStory(token)
      if (response.error) {
        _allStoryStatus.postValue(AllStoryState.Error(response))
      } else {
        _allStoryStatus.postValue(AllStoryState.Success(response))
      }
    }
  }

  fun uploadStory(token: String, data: UploadStoryPostModel) {
    viewModelScope.launch {
      _uploadStoryStatus.postValue(UploadStoryState.Loading)
      val response = storyRepository.uploadStory(token, data)
      if (response.error) {
        _uploadStoryStatus.postValue(UploadStoryState.Error(response))
      } else {
        _uploadStoryStatus.postValue(UploadStoryState.Success(response))
      }
    }
  }

  sealed interface UploadStoryState {
    data class Success(val data: RegisterResponseModel) : UploadStoryState
    data class Error(val data: RegisterResponseModel) : UploadStoryState
    data object Loading : UploadStoryState
    data object Idle : UploadStoryState
  }

  sealed interface AllStoryState {
    data class Success(val data: AllStoryResponseModel) : AllStoryState
    data class Error(val data: AllStoryResponseModel) : AllStoryState
    data object Loading : AllStoryState
    data object Idle : AllStoryState
  }
}