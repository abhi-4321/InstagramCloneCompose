package com.example.instagramclone.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramclone.model.LoginRequest
import com.example.instagramclone.model.ProfileItem
import com.example.instagramclone.network.login.RetrofitInstanceLogin
import com.example.instagramclone.network.main.RetrofitInstanceMain
import com.example.instagramclone.network.main.RetrofitInterfaceMain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private var instanceMain: RetrofitInterfaceMain? = null
    private val instanceLogin = RetrofitInstanceLogin.instance

    private val _uiState = MutableStateFlow<LoginState>(LoginState.Idle)
    val uiState: StateFlow<LoginState> = _uiState // Expose state to UI

    private val _flow = MutableLiveData<ProfileItem>()
    val liveData: LiveData<ProfileItem> get() = _flow

    fun fetchUser() {
        viewModelScope.launch {
            val response = instanceMain!!.getUser()
            Log.d("UserResponse", "${response.isSuccessful} ${response.code()}")
            if (response.isSuccessful && response.body() != null)
                _flow.postValue(response.body())
            else
                _flow.postValue(ProfileItem())
        }
    }

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

    fun initOrUpdateRetrofit(context: Context) {
        instanceMain = RetrofitInstanceMain.getApiService(context)
    }
}