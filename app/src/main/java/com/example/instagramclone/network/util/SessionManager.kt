package com.example.instagramclone.network.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

@SuppressLint("ApplySharedPref", "UseKtx")
object SessionManager {
    private const val AUTH_TOKEN = "auth_token"

    fun saveToken(context: Context, token: String?): Boolean {
        val prefs = context.getSharedPreferences(AUTH_TOKEN,Context.MODE_PRIVATE)
        return prefs.edit().putString(AUTH_TOKEN,token).commit()
    }

    fun fetchToken(context: Context) : String? {
        val prefs = context.getSharedPreferences(AUTH_TOKEN,Context.MODE_PRIVATE)
        return prefs.getString(AUTH_TOKEN,null)
    }
}