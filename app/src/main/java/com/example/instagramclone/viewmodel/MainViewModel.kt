package com.example.instagramclone.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.recursiveFetchLongSparseArray
import com.example.instagramclone.model.Chat
import com.example.instagramclone.model.ChatDisplayUser
import com.example.instagramclone.model.PostDisplay
import com.example.instagramclone.model.ProfileItem
import com.example.instagramclone.model.StoryDisplayUser
import com.example.instagramclone.model.StoryItem
import com.example.instagramclone.network.main.RetrofitInterfaceMain
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.EMPTY_REQUEST
import java.io.File

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
    private val _flowFeed = MutableStateFlow<ApiResponse<List<PostDisplay>>>(ApiResponse.Idle)
    val liveDataFeed: StateFlow<ApiResponse<List<PostDisplay>>> get() = _flowFeed

    fun fetchFeed() {
        viewModelScope.launch {
            _flowFeed.value = ApiResponse.Loading

            val responseFeed = retrofitInterfaceMain.fetchFeed()

            if (responseFeed.isSuccessful && responseFeed.body() != null) {
                _flowFeed.value = ApiResponse.Success(responseFeed.body()!!)
            } else {
                _flowFeed.value = ApiResponse.Failure(responseFeed.errorBody()?.string() ?: responseFeed.code().toString())
            }
        }
    }

    // Chat users

    private val _chatUsersFlow = MutableStateFlow<ApiResponse<List<ChatDisplayUser>>>(ApiResponse.Idle)
    val chatUsersFlow: StateFlow<ApiResponse<List<ChatDisplayUser>>> get() = _chatUsersFlow

    fun fetchChatUsers() {
        viewModelScope.launch {
            _chatUsersFlow.value = ApiResponse.Loading

            val response = retrofitInterfaceMain.fetchChatUsers()

            if (response.isSuccessful && response.body() != null) {
                _chatUsersFlow.value = ApiResponse.Success(response.body()!!)
            } else {
                _chatUsersFlow.value = ApiResponse.Failure(response.errorBody()?.string() ?: response.code().toString())
            }
        }
    }

    private val _allChatUsersFlow = MutableStateFlow<ApiResponse<List<ChatDisplayUser>>>(ApiResponse.Idle)
    val allChatUsersFlow: StateFlow<ApiResponse<List<ChatDisplayUser>>> get() = _allChatUsersFlow

    fun fetchAllChatUsers() {
        viewModelScope.launch {
            _allChatUsersFlow.value = ApiResponse.Loading

            val response = retrofitInterfaceMain.fetchAllChatUsers()

            if (response.isSuccessful && response.body() != null) {
                _allChatUsersFlow.value = ApiResponse.Success(response.body()!!)
            } else {
                _allChatUsersFlow.value = ApiResponse.Failure(response.errorBody()?.string() ?: response.code().toString())
            }
        }
    }

    // Story List

    private val _storiesFlow = MutableStateFlow<ApiResponse<List<StoryItem>>>(ApiResponse.Idle)
    val storiesFlow: StateFlow<ApiResponse<List<StoryItem>>> get() = _storiesFlow

    fun fetchStories(userId: Int) {
        viewModelScope.launch {
            _storiesFlow.value = ApiResponse.Loading

            val response = retrofitInterfaceMain.getStories(userId)

            if (response.isSuccessful && response.body() != null) {
                _storiesFlow.value = ApiResponse.Success(response.body()!!)
            } else {
                _storiesFlow.value = ApiResponse.Failure(response.errorBody()?.string() ?: response.code().toString())
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