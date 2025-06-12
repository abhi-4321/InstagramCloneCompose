package com.example.instagramclone.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.instagramclone.network.main.RetrofitInterfaceMain

class StoryViewModelFactory(private val retrofitInterfaceMain: RetrofitInterfaceMain): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return StoryViewModel(retrofitInterfaceMain) as T
    }
}