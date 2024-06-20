package com.dicoding.drinknt

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.drinknt.databinding.SplashToMainBinding

class SplashToMain : AppCompatActivity() {

    private lateinit var binding: SplashToMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SplashToMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStart.setOnClickListener { moveToLogin() }
    }

    private fun moveToLogin() {
        Log.d(TAG, "Moving to LoginActivity")
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    companion object {
        const val TAG = "SplashToMain"
    }
}