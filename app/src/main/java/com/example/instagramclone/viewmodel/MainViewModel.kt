package com.example.instagramclone.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramclone.model.PostDisplay
import com.example.instagramclone.model.ProfileItem
import com.example.instagramclone.model.StoryDisplayUser
import com.example.instagramclone.network.main.RetrofitInterfaceMain
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val retrofitInterfaceMain: RetrofitInterfaceMain) : ViewModel() {

    // Profile

    private val _flow = MutableStateFlow<ApiResponse<ProfileItem>>(ApiResponse.Idle)
    val liveData: StateFlow<ApiResponse<ProfileItem>> get() = _flow

    fun fetchUser() {
        viewModelScope.launch {
            _flow.value = ApiResponse.Loading
            val response = retrofitInterfaceMain.getUser()
            Log.d("UserResponse", "${response.isSuccessful} ${response.code()}")
            if (response.isSuccessful && response.body() != null)
                _flow.value = ApiResponse.Success(response.body()!!)
            else
                _flow.value = ApiResponse.Failure(response.errorBody()?.string() ?: response.code().toString())
        }
    }

    // Dashboard

    private val _flowStory = MutableStateFlow<ApiResponse<List<StoryDisplayUser>>>(ApiResponse.Idle)
    val liveDataStory: StateFlow<ApiResponse<List<StoryDisplayUser>>> get() = _flowStory

    private val _flowFeed = MutableStateFlow<ApiResponse<List<PostDisplay>>>(ApiResponse.Idle)
    val liveDataFeed: StateFlow<ApiResponse<List<PostDisplay>>> get() = _flowFeed

    fun fetchFeed() {
        viewModelScope.launch {
            _flowStory.value = ApiResponse.Loading
            _flowFeed.value = ApiResponse.Loading
            val responseStory = async {
                retrofitInterfaceMain.fetchDisplayUsers()
            }
            val responseFeed = async {
                retrofitInterfaceMain.fetchFeed()
            }

            val storyRes = responseStory.await()
            val feedRes = responseFeed.await()

            if (storyRes.isSuccessful && storyRes.body() != null) {
                _flowStory.value = ApiResponse.Success(storyRes.body()!!)
            } else {
                _flowStory.value = ApiResponse.Failure(storyRes.errorBody()?.string() ?: storyRes.code().toString())
            }

            if (storyRes.isSuccessful && storyRes.body() != null) {
                _flowFeed.value = ApiResponse.Success(feedRes.body()!!)
            } else {
                _flowFeed.value = ApiResponse.Failure(feedRes.errorBody()?.string() ?: feedRes.code().toString())
            }
        }
    }

    sealed class ApiResponse<out T> {
        data class Success<T>(val data: T?) : ApiResponse<T>()
        data class Failure(val error: String?): ApiResponse<Nothing>()
        data object Loading: ApiResponse<Nothing>()
        data object Idle: ApiResponse<Nothing>()
    }
}