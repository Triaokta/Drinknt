package com.dicoding.drinknt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.drinknt.databinding.ActivityCsBinding

class CsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}