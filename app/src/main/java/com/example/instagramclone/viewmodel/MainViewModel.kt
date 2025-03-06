package com.example.instagramclone.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.instagramclone.model.LoginRequest
import com.example.instagramclone.model.Profile
import com.example.instagramclone.network.util.SessionManager
import com.example.instagramclone.network.login.RetrofitInstanceLogin
import com.example.instagramclone.network.main.RetrofitInstanceMain
import com.example.instagramclone.network.main.RetrofitInterfaceMain
import kotlinx.coroutines.launch

class MainViewModel(retrofitInstanceMain: RetrofitInterfaceMain, private val sessionManager: SessionManager) : ViewModel() {
    private var instanceMain = retrofitInstanceMain
    private val instanceLogin = RetrofitInstanceLogin.instance

    private val _flow = MutableLiveData<Profile>()
    val liveData: LiveData<Profile> get() = _flow

    fun addUser(loginRequest: LoginRequest): Boolean {
        viewModelScope.launch {
            instanceLogin.register(loginRequest)
        }
        return true
    }

    fun fetchUser() {
        viewModelScope.launch {
            val response = instanceMain.getUser()
            Log.d("UserResponse", "${response.isSuccessful} ${response.code()}")
            if (response.isSuccessful && response.body() != null)
                _flow.postValue(response.body())
            else
                _flow.postValue(Profile())
        }
    }

    suspend fun login(loginRequest: LoginRequest, context: Context) : Boolean {
        val response = instanceLogin.login(loginRequest)
        val bool = response.isSuccessful && response.body() != null
        if (bool) {
            sessionManager.saveAuthToken(response.body()!!.token)
            updateRetrofitInstance(context)
        }
        return bool
    }

    fun updateBio(id: String, bio: String): Boolean {
        viewModelScope.launch {
//            instance.updateBio(id, bio)
        }
        return true
    }

    fun deleteUser(id: String): Boolean {
        viewModelScope.launch {
//            instance.deleteUser(id)
        }
        return true
    }

    fun getUserList(): List<Profile> {
        viewModelScope.launch {
//            instance.getUserList()
        }
        return emptyList()
    }

    fun updateRetrofitInstance(context: Context) {
        instanceMain = RetrofitInstanceMain().getApiService(context)
    }

    @Suppress("UNCHECKED_CAST")
    class MainViewModelFactory(private val retrofitInstanceMain: RetrofitInterfaceMain, private val sessionManager: SessionManager) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(retrofitInstanceMain, sessionManager) as T
        }
    }
}