package com.dicoding.drinknt.retrofit

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import com.dicoding.drinknt.ScanActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.IOException

class ApiRequest(
    private val context: Context,
    private val classifierListener: ApiRequestListener?
) {

    interface ApiRequestListener {
        fun onError(errorMsg: String)
        fun onResults(results: String?, inferenceTime: Long)
    }

    fun classifyImage(imageUri: Uri) {
        val bitmap = getImageBitmap(imageUri)
        val inferenceTime = SystemClock.uptimeMillis()
        sendImageToServer(bitmap, inferenceTime)
    }

    private fun getImageBitmap(imageUri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        }
    }

    private fun sendImageToServer(bitmap: Bitmap, startTime: Long) {
        val client = OkHttpClient()
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageData = byteArrayOutputStream.toByteArray()

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", "image.jpg", RequestBody.create("image/jpeg".toMediaTypeOrNull(), imageData))
            .build()

        val request = Request.Builder()
            .url("https://ml-model-api-kwct44cyka-et.a.run.app/predict")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.e(TAGIMAGE, "Failed to connect to server: ${e.message}")
                (context as ScanActivity).runOnUiThread {
                    classifierListener?.onError("Failed to connect to server")
                }
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val elapsedTime = SystemClock.uptimeMillis() - startTime
                if (!response.isSuccessful) {
                    Log.e(TAGIMAGE, "Server error: ${response.message}")
                    (context as ScanActivity).runOnUiThread {
                        classifierListener?.onError("Server error: ${response.message}")
                    }
                    return
                }

                response.body?.let { responseBody ->
                    val responseString = responseBody.string()
                    Log.d(TAGIMAGE, "Server response: $responseString")
                    (context as ScanActivity).runOnUiThread {
                        classifierListener?.onResults(responseString, elapsedTime)
                    }
                } ?: run {
                    (context as ScanActivity).runOnUiThread {
                        classifierListener?.onError("Empty response from server")
                    }
                }
            }
        })
    }

    companion object {
        private const val TAGIMAGE = "ApiRequest"
    }
}
