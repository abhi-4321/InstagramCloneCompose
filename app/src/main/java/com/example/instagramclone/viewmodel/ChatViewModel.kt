package com.example.instagramclone.viewmodel

import androidx.lifecycle.ViewModel
import com.example.instagramclone.model.Chat
import com.example.instagramclone.network.util.ChatWebSocketListener
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

class ChatViewModel(userId: Int) : ViewModel() {

    private val webSocket: WebSocket

    val _message: MutableStateFlow<MessageResponse> = MutableStateFlow(MessageResponse.Idle)
    val message: StateFlow<MessageResponse> get() = _message.asStateFlow()

    init {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("ws://192.168.1.39:3000?userId=$userId")  // replace with your actual URL
            .build()

        val listener = ChatWebSocketListener { chat: Chat ->
            _message.value = MessageResponse.MessageReceived(chat)
        }
        webSocket = client.newWebSocket(request, listener)
    }

    fun sendMessage(chat: Chat) {
        val gson = Gson().toJson(chat)
        webSocket.send(gson)
    }

    sealed class MessageResponse {
        data class MessageReceived(val chat: Chat) : MessageResponse()
        data object Idle : MessageResponse()
    }
}