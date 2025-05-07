package com.example.instagramclone.worker

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.instagramclone.network.main.RetrofitInstanceMain
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
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
        try {
            Log.d("WorkerTag", "doWork: Starting worker")

            val captionPart =
                if (caption.isNotEmpty())
                    caption.toRequestBody("text/plain".toMediaTypeOrNull())
                else
                    EMPTY_REQUEST

            val contentResolver = applicationContext.contentResolver
            val filename = getFileNameFromUri(imageUri.toUri()) ?: "image.jpg"

            contentResolver.openInputStream(imageUri.toUri())?.use { inputStream ->
                val requestBody = inputStream.readBytes().toRequestBody("image/*".toMediaTypeOrNull())

                val imagePart = MultipartBody.Part.createFormData(
                    "image",
                    filename,
                    requestBody
                )

                Log.d("WorkerTag", "Image Part: ${imagePart.body}")
                val response = retrofitInterfaceMain.createPost(captionPart,imagePart)
                // Rest of your code...
                Log.d("WorkerTag", "Response : $response")

                return if (response.isSuccessful) {
                    Result.Success()
                } else {
                    if (counter > 3) {
                        Result.failure()
                    }
                    counter++
                    Result.retry()
                }
            } ?: run {
                Log.d("WorkerTag", "Failed to open input stream")
                return Result.failure()
            }
        } catch (e: Exception) {
            Log.d("WorkerTag", "${e.message}")
            return Result.failure()
        }
    }

    private fun getFileNameFromUri(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = applicationContext.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val columnIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (columnIndex != -1) {
                        result = it.getString(columnIndex)
                    }
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != -1) {
                result = result?.substring(cut!! + 1)
            }
        }
        return result
    }
}