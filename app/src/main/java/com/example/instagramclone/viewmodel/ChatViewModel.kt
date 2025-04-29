package com.example.instagramclone.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramclone.model.Chat
import com.example.instagramclone.network.main.RetrofitInterfaceMain
import com.example.instagramclone.network.util.ChatWebSocketListener
import com.example.instagramclone.viewmodel.MainViewModel.ApiResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okhttp3.internal.notify
import okio.ByteString

class ChatViewModel(userId: Int, private val retrofitInterfaceMain: RetrofitInterfaceMain, mainViewModel: MainViewModel) :
    ViewModel() {

    val client = OkHttpClient()
    val request = Request.Builder()
        .url("ws://192.168.1.38:3000?userId=$userId")  // replace with your actual URL
        .build()

    val listener = object : WebSocketListener() {
        val TAG = "WebSocketListener"

        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d(TAG,"Connected to WebSocket")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d(TAG, "Raw message received: $text")
            try {
                // Otherwise try to parse as a Chat message
                val gson = Gson()
                val msg = gson.fromJson(text, Chat::class.java)
                Log.d(TAG, "Parsed chat message: $msg")

                if (msg.senderId == userId) {
                    Log.d(TAG, "Acknowledgement received: $msg")
                }

                updateLastMessage(msg)
                mainViewModel.fetchChatUsers()
            } catch (e: Exception) {
                Log.e(TAG, "Error parsing message: ${e.message}", e)
                e.printStackTrace()
            }
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            Log.d(TAG, "Received bytes: ${bytes.hex()}")
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            Log.d(TAG, "Closing WebSocket: $reason")
            webSocket.close(1000, null)
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
            Log.d(TAG, "Closed WebSocket: $reason")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.d(TAG, "Error: ${t.message}")
        }
    }

    private val webSocket = client.newWebSocket(request, listener)

    fun sendMessage(chat: Chat) {
        val gson = Gson().toJson(chat)
        val res = webSocket.send(gson)
        Log.d("WebSocketListener", res.toString())
    }

    private val _previousChats = MutableStateFlow<ApiResponse<MutableList<Chat>>>(ApiResponse.Idle)
    val previousChats: StateFlow<ApiResponse<List<Chat>>> get() = _previousChats

    fun fetchPreviousChats(receiverId: Int) {
        viewModelScope.launch {
            _previousChats.value = ApiResponse.Loading

            val response = retrofitInterfaceMain.fetchPreviousChats(receiverId)

            Log.d("Chat Response", "${response.body()}")

            if (response.isSuccessful && response.body() != null) {
                _previousChats.value = ApiResponse.Success(response.body()!!.toMutableList())
            } else {
                _previousChats.value = ApiResponse.Failure(
                    response.errorBody()?.string() ?: response.code().toString()
                )
            }
        }
    }

    private fun updateLastMessage(chat: Chat) {
        val previousChats = (_previousChats.value as? ApiResponse.Success)?.data ?: return

        // Create a new list (copy)
        val updatedChats = previousChats.toMutableList()

        // Add the new chat
        updatedChats.add(updatedChats.size, chat)

        // Update the state flow with the new list
        _previousChats.value = ApiResponse.Success(updatedChats)
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("ChatViewModel", "ViewModel cleared")
        webSocket.close(1000, "ViewModel cleared")
    }
}