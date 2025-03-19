package com.example.instagramclone.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramclone.model.LoginRequest
import com.example.instagramclone.model.PostDisplay
import com.example.instagramclone.model.ProfileItem
import com.example.instagramclone.model.StoryDisplayUser
import com.example.instagramclone.network.login.RetrofitInstanceLogin
import com.example.instagramclone.network.main.RetrofitInstanceMain
import com.example.instagramclone.network.main.RetrofitInterfaceMain
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private var instanceMain: RetrofitInterfaceMain? = null
    private val instanceLogin = RetrofitInstanceLogin.instance

    // Login

    private val _uiState = MutableStateFlow<LoginState>(LoginState.Idle)
    val uiState: StateFlow<LoginState> = _uiState // Expose state to UI

    fun login(loginRequest: LoginRequest) {
        viewModelScope.launch {
            _uiState.value = LoginState.Loading // Show loading UI

            try {
                val response = instanceLogin.login(loginRequest)
                if (response.isSuccessful && response.body() != null) {
                    _uiState.value = LoginState.Success(token = response.body()!!.token)
                }else {
                    _uiState.value = LoginState.Error("Invalid credentials")
                }
            } catch (e: Exception) {
                _uiState.value = LoginState.Error("Network error")
            }
        }
    }

    sealed class LoginState {
        data object Idle : LoginState()
        data object Loading : LoginState()
        data class Success(val token: String) : LoginState()
        data class Error(val message: String) : LoginState()
    }

    // Profile

    private val _flow = MutableStateFlow<ApiResponse<ProfileItem>>(ApiResponse.Idle)
    val liveData: StateFlow<ApiResponse<ProfileItem>> get() = _flow

    fun fetchUser() {
        viewModelScope.launch {
            _flow.value = ApiResponse.Loading
            val response = instanceMain!!.getUser()
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
                instanceMain!!.fetchDisplayUsers()
            }
            val responseFeed = async {
                instanceMain!!.fetchFeed()
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

    // Re-instantiate retrofit

    fun initOrUpdateRetrofit(context: Context) {
        instanceMain = RetrofitInstanceMain.getApiService(context)
    }

    sealed class ApiResponse<out T> {
        data class Success<T>(val data: T?) : ApiResponse<T>()
        data class Failure(val error: String?): ApiResponse<Nothing>()
        data object Loading: ApiResponse<Nothing>()
        data object Idle: ApiResponse<Nothing>()
    }
}