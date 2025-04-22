package com.example.instagramclone.network.util

import android.util.Log
import com.example.instagramclone.model.Chat
import com.google.gson.Gson
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class ChatWebSocketListener(private val onMessage: (chat: Chat) -> Unit) : WebSocketListener() {

    val TAG = "WebSocketListener"

    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.d(TAG,"Connected to WebSocket")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        val gson = Gson()
        val msg = gson.fromJson(text,Chat::class.java)
        onMessage(msg)
        Log.d(TAG, "Received message: $msg")
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        Log.d(TAG, "Received bytes: ${bytes.hex()}")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        Log.d(TAG, "Closing WebSocket: $reason")
        webSocket.close(1000, null)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.d(TAG, "Error: ${t.message}")
    }
}