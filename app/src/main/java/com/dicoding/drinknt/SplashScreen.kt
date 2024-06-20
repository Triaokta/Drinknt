package com.dicoding.drinknt

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.drinknt.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgDrinknt.animate()
            .setDuration(3000)
            .alpha(0f)
            .withEndAction {
                val intent = Intent(this, SplashToMain::class.java)
                startActivity(intent)
                finish()
            }
    }
}
