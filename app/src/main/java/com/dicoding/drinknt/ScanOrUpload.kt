package com.dicoding.drinknt

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.drinknt.databinding.ScanOrUploadBinding

class ScanOrUpload : AppCompatActivity() {

    private lateinit var binding: ScanOrUploadBinding
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ScanOrUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCamera.setOnClickListener { startCamera() }
        binding.btnUpload.setOnClickListener { startGallery() }
    }

    private fun moveToScan() {
        currentImageUri?.let { uri ->
            Log.d(MainActivity.TAG, "Moving to ScanActivity without results")
            val intent = Intent(this, ScanActivity::class.java)
            intent.putExtra(ScanActivity.IMAGE_URI, uri.toString())
            startActivity(intent)
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            moveToScan()
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            moveToScan()
        } else {
            Log.d(MainActivity.TAG, "No media selected")
        }
    }
}