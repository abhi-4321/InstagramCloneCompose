package com.example.instagramclone.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.instagramclone.model.LoginRequest
import com.example.instagramclone.model.Profile
import com.example.instagramclone.network.login.RetrofitInstanceLogin
import com.example.instagramclone.network.main.RetrofitInterfaceMain
import kotlinx.coroutines.launch

class MainViewModel(private val instanceMain: RetrofitInterfaceMain) : ViewModel() {
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

    suspend fun login(loginRequest: LoginRequest) : String? {
        val response = instanceLogin.login(loginRequest)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!.token
        }
        return null
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

    @Suppress("UNCHECKED_CAST")
    class MainViewModelFactory(private val retrofitInstanceMain: RetrofitInterfaceMain) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(retrofitInstanceMain) as T
        }
    }
}