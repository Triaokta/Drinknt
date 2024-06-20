package com.dicoding.drinknt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.drinknt.databinding.RegisterGoogleBinding

class FbRegister : AppCompatActivity() {

    private lateinit var binding: RegisterGoogleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegisterGoogleBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}