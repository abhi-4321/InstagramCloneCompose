package com.example.instagramclone.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramclone.model.Profile
import com.example.instagramclone.network.RetrofitInstance
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val instance = RetrofitInstance.instance

    private val _flow = MutableLiveData<Profile>()
    val liveData: LiveData<Profile> get() = _flow

    fun addUser(user: Profile): Boolean {
        viewModelScope.launch {
            instance.addUser(user)
        }
        return true
    }

    fun fetchUser(id: String) {
        viewModelScope.launch {
            val response = instance.getUser(id)
            Log.d("UserResponse", "${response.isSuccessful} ${response.code()}")
            if (response.isSuccessful && response.body() != null)
                _flow.postValue(response.body())
            else
                _flow.postValue(Profile())
        }
    }

    fun updateBio(id: String, bio: String): Boolean {
        viewModelScope.launch {
            instance.updateBio(id, bio)
        }
        return true
    }

    fun deleteUser(id: String): Boolean {
        viewModelScope.launch {
            instance.deleteUser(id)
        }
        return true
    }

    fun getUserList(): List<Profile> {
        viewModelScope.launch {
            instance.getUserList()
        }
        return emptyList()
    }

/*
    sealed class Response {
        data object Idle : Response()
        data class Success(val data: Profile?) : Response()
        data class Failure(val code: String) : Response()
    }
*/

}