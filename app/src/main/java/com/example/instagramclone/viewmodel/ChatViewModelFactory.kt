package com.example.instagramclone.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.instagramclone.model.Chat
import com.example.instagramclone.network.main.RetrofitInterfaceMain

class ChatViewModelFactory(private val userId: Int, private val retrofitInterfaceMain: RetrofitInterfaceMain) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatViewModel(userId, retrofitInterfaceMain) as T
    }
}