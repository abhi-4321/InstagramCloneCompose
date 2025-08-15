package com.example.instagramclone.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramclone.model.ChatDisplayUser
import com.example.instagramclone.model.FollowUserItem
import com.example.instagramclone.model.LikeInfo
import com.example.instagramclone.model.PostDisplay
import com.example.instagramclone.model.ProfileItem
import com.example.instagramclone.model.SearchResponse
import com.example.instagramclone.model.StoryItem
import com.example.instagramclone.network.main.RetrofitInterfaceMain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val retrofitInterfaceMain: RetrofitInterfaceMain) : ViewModel() {

    // Following List
    private val _flowFollowingList = MutableStateFlow<ApiResponse<FollowUserItem>>(
        ApiResponse.Idle
    )
    val flowFollowingList: StateFlow<ApiResponse<FollowUserItem>> get() = _flowFollowingList

    fun fetchFollowing() {
        viewModelScope.launch {
            _flowFollowingList.emit(ApiResponse.Loading)

            val response = retrofitInterfaceMain.getFollowing()

            if (response.isSuccessful && response.body() != null)
                _flowFollowingList.emit(ApiResponse.Success(response.body()!!))
            else
                _flowFollowingList.emit(
                    ApiResponse.Failure(
                        response.errorBody()?.string() ?: response.code().toString()
                    )
                )
        }
    }


    // My Profile
    private val _flow = MutableStateFlow<ApiResponse<ProfileItem>>(
        ApiResponse.Idle
    )
    val liveData: StateFlow<ApiResponse<ProfileItem>> get() = _flow

    fun fetchUser() {
        viewModelScope.launch {
            _flow.emit(ApiResponse.Loading)
            val response = retrofitInterfaceMain.getUser()
            Log.d("UserResponse", "${response.isSuccessful} ${response.code()}")
            if (response.isSuccessful && response.body() != null)
                _flow.emit(ApiResponse.Success(response.body()!!))
            else
                _flow.emit(
                    ApiResponse.Failure(
                        response.errorBody()?.string() ?: response.code().toString()
                    )
                )
        }
    }

    // User Profile by ID
    private val _flowUserProfile = MutableStateFlow<ApiResponse<ProfileItem>>(
        ApiResponse.Idle
    )
    val flowUserProfile: StateFlow<ApiResponse<ProfileItem>> get() = _flowUserProfile

    fun fetchUserProfile(id: Int) {
        viewModelScope.launch {
            _flowUserProfile.emit(ApiResponse.Loading)

            val response = retrofitInterfaceMain.getUserById(id)
            Log.d("UserResponse", "${response.isSuccessful} ${response.code()}")
            if (response.isSuccessful && response.body() != null)
                _flowUserProfile.emit(ApiResponse.Success(response.body()!!))
            else
                _flowUserProfile.emit(
                    ApiResponse.Failure(
                        response.errorBody()?.string() ?: response.code().toString()
                    )
                )
        }
    }

    // Dashboard
    private val _flowFeed = MutableStateFlow<ApiResponse<List<PostDisplay>>>(ApiResponse.Idle)
    val liveDataFeed: StateFlow<ApiResponse<List<PostDisplay>>> get() = _flowFeed

    fun fetchFeed(uId: Int) {
        viewModelScope.launch {
            _flowFeed.emit(ApiResponse.Loading)

            val responseFeed = retrofitInterfaceMain.fetchFeed()

            if (responseFeed.isSuccessful && responseFeed.body() != null) {
                setPosts(responseFeed.body()!!,uId)
                _flowFeed.emit(ApiResponse.Success(responseFeed.body()!!))
            } else {
                _flowFeed.emit(
                    ApiResponse.Failure(
                        responseFeed.errorBody()?.string() ?: responseFeed.code().toString()
                    )
                )
            }
        }
    }

    // Explore

    private val _flowExplore = MutableStateFlow<ApiResponse<List<PostDisplay>>>(ApiResponse.Idle)
    val flowExplore: StateFlow<ApiResponse<List<PostDisplay>>> get() = _flowExplore

    fun fetchExplore() {
        viewModelScope.launch {
            _flowExplore.emit(ApiResponse.Loading)

            val responseFeed = retrofitInterfaceMain.explorePosts()

            if (responseFeed.isSuccessful && responseFeed.body() != null) {
                _flowExplore.emit(ApiResponse.Success(responseFeed.body()!!))
            } else {
                _flowExplore.emit(
                    ApiResponse.Failure(
                        responseFeed.errorBody()?.string() ?: responseFeed.code().toString()
                    )
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
            _chatUsersFlow.emit(ApiResponse.Loading)

            val response = retrofitInterfaceMain.fetchChatUsers()

            if (response.isSuccessful && response.body() != null) {
                _chatUsersFlow.emit(ApiResponse.Success(response.body()!!))
            } else {
                _chatUsersFlow.emit(
                    ApiResponse.Failure(
                        response.errorBody()?.string() ?: response.code().toString()
                    )
                )
            }
        }
    }

    private val _allChatUsersFlow =
        MutableStateFlow<ApiResponse<List<ChatDisplayUser>>>(ApiResponse.Idle)
    val allChatUsersFlow: StateFlow<ApiResponse<List<ChatDisplayUser>>> get() = _allChatUsersFlow

    fun fetchAllChatUsers() {
        viewModelScope.launch {
            _allChatUsersFlow.emit(ApiResponse.Loading)

            val response = retrofitInterfaceMain.fetchAllChatUsers()

            if (response.isSuccessful && response.body() != null) {
                _allChatUsersFlow.emit(ApiResponse.Success(response.body()!!))
            } else {
                _allChatUsersFlow.emit(
                    ApiResponse.Failure(
                        response.errorBody()?.string() ?: response.code().toString()
                    )
                )
            }
        }
    }

    // Story List

    private val _storiesFlow = MutableStateFlow<ApiResponse<List<StoryItem>>>(ApiResponse.Idle)
    val storiesFlow: StateFlow<ApiResponse<List<StoryItem>>> get() = _storiesFlow

    fun fetchStories(userId: Int) {
        viewModelScope.launch {
            _storiesFlow.emit(ApiResponse.Loading)

            val response = retrofitInterfaceMain.getStories(userId)

            if (response.isSuccessful && response.body() != null) {
                _storiesFlow.emit(ApiResponse.Success(response.body()!!))
            } else {
                _storiesFlow.emit(
                    ApiResponse.Failure(
                        response.errorBody()?.string() ?: response.code().toString()
                    )
                )
            }
        }
    }

    // Like
    val likesStateMap = mutableStateMapOf<Int, LikeInfo>()

    fun setPosts(posts: List<PostDisplay>, uId: Int) {
        likesStateMap.clear()
        for (post in posts) {
            likesStateMap[post.id] = LikeInfo(post.likedBy.contains(uId), post.likesCount.toInt())
        }
    }

    fun toggleLikePost(postId: Int) {
        val old = likesStateMap[postId] ?: LikeInfo(false, 0)
        likesStateMap[postId] = old.copy(liked = !old.liked, count = old.count + if (old.liked) -1 else 1)

        viewModelScope.launch {
            val result = runCatching { retrofitInterfaceMain.like(postId) }.getOrNull()
            if (result == null || !result.isSuccessful) {
                likesStateMap[postId] = old // rollback
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

    // Follow User
    fun followUser() {
    }

    sealed class ApiResponse<out T> {
        data class Success<T>(val data: T?) : ApiResponse<T>()
        data class Failure(val error: String?) : ApiResponse<Nothing>()
        data object Loading : ApiResponse<Nothing>()
        data object Idle : ApiResponse<Nothing>()
    }
}