package com.example.instagramclone.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramclone.model.ChatDisplayUser
import com.example.instagramclone.model.Comment
import com.example.instagramclone.model.PostDisplay
import com.example.instagramclone.model.ProfileItem
import com.example.instagramclone.model.SearchResponse
import com.example.instagramclone.model.StoryItem
import com.example.instagramclone.network.main.RetrofitInterfaceMain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val retrofitInterfaceMain: RetrofitInterfaceMain) : ViewModel() {

    // My Profile
    private val _flow = MutableStateFlow<ApiResponse<ProfileItem>>(
        ApiResponse.Success(
            ProfileItem()
        )
    )
    val liveData: StateFlow<ApiResponse<ProfileItem>> get() = _flow

    fun fetchUser() {
        viewModelScope.launch {
            _flow.value = ApiResponse.Loading
            val response = retrofitInterfaceMain.getUser()
            Log.d("UserResponse", "${response.isSuccessful} ${response.code()}")
            if (response.isSuccessful && response.body() != null)
                _flow.value = ApiResponse.Success(response.body()!!)
            else
                _flow.value = ApiResponse.Failure(
                    response.errorBody()?.string() ?: response.code().toString()
                )
        }
    }

    // User Profile by ID
    private val _flowUserProfile = MutableStateFlow<ApiResponse<ProfileItem>>(
        ApiResponse.Success(
            ProfileItem()
        )
    )
    val flowUserProfile: StateFlow<ApiResponse<ProfileItem>> get() = _flow

    fun fetchUserProfile(id: Int) {
        viewModelScope.launch {
            _flowUserProfile.value = ApiResponse.Loading

            val response = retrofitInterfaceMain.getUserById(id)
            Log.d("UserResponse", "${response.isSuccessful} ${response.code()}")
            if (response.isSuccessful && response.body() != null)
                _flowUserProfile.value = ApiResponse.Success(response.body()!!)
            else
                _flowUserProfile.value = ApiResponse.Failure(
                    response.errorBody()?.string() ?: response.code().toString()
                )
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
                _flowFeed.value = ApiResponse.Failure(
                    responseFeed.errorBody()?.string() ?: responseFeed.code().toString()
                )
            }
        }
    }

    // Explore

    private val _flowExplore = MutableStateFlow<ApiResponse<List<PostDisplay>>>(ApiResponse.Idle)
    val flowExplore: StateFlow<ApiResponse<List<PostDisplay>>> get() = _flowExplore

    fun fetchExplore() {
        viewModelScope.launch {
            _flowExplore.value = ApiResponse.Loading

            val responseFeed = retrofitInterfaceMain.explorePosts()

            if (responseFeed.isSuccessful && responseFeed.body() != null) {
                _flowExplore.value = ApiResponse.Success(responseFeed.body()!!)
            } else {
                _flowExplore.value = ApiResponse.Failure(
                    responseFeed.errorBody()?.string() ?: responseFeed.code().toString()
                )
            }
        }
    }

    // Chat users

    private val _chatUsersFlow =
        MutableStateFlow<ApiResponse<List<ChatDisplayUser>>>(ApiResponse.Idle)
    val chatUsersFlow: StateFlow<ApiResponse<List<ChatDisplayUser>>> get() = _chatUsersFlow

    fun fetchChatUsers() {
        viewModelScope.launch {
            _chatUsersFlow.value = ApiResponse.Loading

            val response = retrofitInterfaceMain.fetchChatUsers()

            if (response.isSuccessful && response.body() != null) {
                _chatUsersFlow.value = ApiResponse.Success(response.body()!!)
            } else {
                _chatUsersFlow.value = ApiResponse.Failure(
                    response.errorBody()?.string() ?: response.code().toString()
                )
            }
        }
    }

    private val _allChatUsersFlow =
        MutableStateFlow<ApiResponse<List<ChatDisplayUser>>>(ApiResponse.Idle)
    val allChatUsersFlow: StateFlow<ApiResponse<List<ChatDisplayUser>>> get() = _allChatUsersFlow

    fun fetchAllChatUsers() {
        viewModelScope.launch {
            _allChatUsersFlow.value = ApiResponse.Loading

            val response = retrofitInterfaceMain.fetchAllChatUsers()

            if (response.isSuccessful && response.body() != null) {
                _allChatUsersFlow.value = ApiResponse.Success(response.body()!!)
            } else {
                _allChatUsersFlow.value = ApiResponse.Failure(
                    response.errorBody()?.string() ?: response.code().toString()
                )
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
                _storiesFlow.value = ApiResponse.Failure(
                    response.errorBody()?.string() ?: response.code().toString()
                )
            }
        }
    }

    // Like

    fun toggleLikePost(postId: Int, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val response = retrofitInterfaceMain.like(postId) // Your toggle API call
                if (response.isSuccessful) {
                    // Optionally refresh specific post data or entire feed
                    // fetchFeed()
                    callback(true)
                } else {
                    callback(false)
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error toggling like: ${e.message}")
                callback(false)
            }
        }
    }

    // Search
    private val _searchResults = MutableStateFlow<ApiResponse<SearchResponse>>(ApiResponse.Idle)
    val searchResults: StateFlow<ApiResponse<SearchResponse>> get() = _searchResults

    fun searchUsers(query: String) {
        viewModelScope.launch {
            _searchResults.emit(ApiResponse.Loading)
            try {
                val result = retrofitInterfaceMain.searchUsers(query) // suspend call
                _searchResults.emit(ApiResponse.Success(result))
            } catch (e: Exception) {
                _searchResults.emit(ApiResponse.Failure(e.message ?: "Error"))
            }
        }
    }

    sealed class ApiResponse<out T> {
        data class Success<T>(val data: T?) : ApiResponse<T>()
        data class Failure(val error: String?) : ApiResponse<Nothing>()
        data object Loading : ApiResponse<Nothing>()
        data object Idle : ApiResponse<Nothing>()
    }
}