package com.dicoding.drinknt

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.drinknt.databinding.ActivityScanBinding
import com.dicoding.drinknt.retrofit.ApiRequest

class ScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanBinding
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentImageUri = intent.getStringExtra(IMAGE_URI)?.let { Uri.parse(it) }

        binding.ivPreview.setImageURI(currentImageUri)

        binding.btnScan.setOnClickListener {
            currentImageUri?.let {
                scanImage()
            } ?: run {
                showToast(getString(R.string.image_classifier_failed))
            }
        }
    }

    private fun scanImage() {
        currentImageUri?.let { uri ->
            val apiRequest = ApiRequest(this, object : ApiRequest.ApiRequestListener {
                override fun onError(errorMsg: String) {
                    showToast(errorMsg)
                }

                override fun onResults(results: String?, inferenceTime: Long) {
                    moveToResult(results, uri)
                }
            })
            apiRequest.classifyImage(uri)
        } ?: showToast(getString(R.string.image_classifier_failed))
    }

    private fun moveToResult(results: String?, imageUri: Uri) {
        Log.d(TAG, "Moving to ResultActivity")
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(ResultActivity.IMAGE_URI, imageUri.toString())
        intent.putExtra(ResultActivity.RESULTS, results)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val TAG = "ScanActivity"
        const val IMAGE_URI = "image_uri"
    }
}