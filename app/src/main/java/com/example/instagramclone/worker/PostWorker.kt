package com.example.instagramclone.worker

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.instagramclone.network.main.RetrofitInstanceMain
import com.example.instagramclone.viewmodel.MainViewModel.ApiResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.EMPTY_REQUEST
import java.io.File

class PostWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    val caption = workerParams.inputData.getString("caption") ?: ""
    val imageUri = workerParams.inputData.getString("uri") ?: ""
    val token = workerParams.inputData.getString("token") ?: ""

    var counter = 0

    private val retrofitInterfaceMain = RetrofitInstanceMain.getApiService(token)

    @SuppressLint("RestrictedApi")
    override suspend fun doWork(): Result {
        val rb =
            if (caption.isNotEmpty()) caption.toRequestBody("txt".toMediaTypeOrNull()) else EMPTY_REQUEST

        val response = retrofitInterfaceMain.createPost(rb, File(imageUri))

        return if (response.isSuccessful && response.body() != null) {
            Result.Success()
        } else {
            if (counter > 3) {
                Result.failure()
            }
            counter++
            Result.retry()
        }
    }
}