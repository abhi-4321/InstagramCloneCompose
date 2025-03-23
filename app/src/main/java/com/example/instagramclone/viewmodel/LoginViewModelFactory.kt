package com.example.instagramclone.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.instagramclone.network.login.RetrofitInterfaceLogin

class LoginViewModelFactory(private val retrofitInterfaceLogin: RetrofitInterfaceLogin) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(retrofitInterfaceLogin) as T
    }
}