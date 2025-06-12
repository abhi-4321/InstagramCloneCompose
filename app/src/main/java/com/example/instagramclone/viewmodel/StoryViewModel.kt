package com.example.instagramclone.viewmodel

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramclone.model.StoryDisplayUser
import com.example.instagramclone.network.main.RetrofitInterfaceMain
import com.example.instagramclone.viewmodel.MainViewModel.ApiResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StoryViewModel(private val retrofitInterfaceMain: RetrofitInterfaceMain) : ViewModel() {

    // Display Users

    private val _flowStory = MutableStateFlow<ApiResponse<List<StoryDisplayUser>>>(ApiResponse.Idle)
    val liveDataStory: StateFlow<ApiResponse<List<StoryDisplayUser>>> get() = _flowStory

    fun fetchDisplayUsers() {
        viewModelScope.launch {
            _flowStory.value = ApiResponse.Loading

            val response = retrofitInterfaceMain.fetchDisplayUsers()

            if (response.isSuccessful && response.body() != null) {
                _flowStory.value = ApiResponse.Success(response.body()!!)
            } else {
                _flowStory.value = ApiResponse.Failure(response.errorBody()?.string() ?: response.code().toString())
            }
        }
    }
}