package com.example.instagramclone.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.instagramclone.network.main.RetrofitInterfaceMain

class MainViewModelFactory(private val retrofitInterfaceMain: RetrofitInterfaceMain): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(retrofitInterfaceMain) as T
    }
}