package com.dicoding.drinknt

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.drinknt.data.SsPreferences
import com.dicoding.drinknt.data.UserResponse
import com.dicoding.drinknt.databinding.ActivityMainBinding
import com.dicoding.drinknt.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var ssPreferences: SsPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnTindakan.setOnClickListener { moveToScanOrUpload() }
        binding.btnRiwayat.setOnClickListener { moveToRiwayat() }
        binding.btnInfo.setOnClickListener { moveToInfo() }
        binding.btnCS.setOnClickListener { moveToCS() }
        binding.ivAvatar.setOnClickListener { moveToProfile()}

        ssPreferences = SsPreferences.getInstance(this.dataStore)

        // Ambil token dari SsPreferences
        lifecycleScope.launch {
            ssPreferences.getToken().collect { token ->
                if (token.isNotEmpty()) {
                    getUserInfo(token)
                }
            }
        }
    }

    private fun getUserInfo(token: String) {
        val client = ApiConfig.getApiService().getUserInfo("Bearer $token")
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val username = response.body()?.username
                    binding.tvusername.text = username
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                // Handle failure
            }
        })
    }


    private fun moveToProfile() {
        Log.d(TAG, "Moving to Profile")
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    private fun moveToScanOrUpload() {
        Log.d(TAG, "Moving to Scan Or Upload")
        val intent = Intent(this, ScanOrUpload::class.java)
        startActivity(intent)
    }

    private fun moveToRiwayat() {
        Log.d(TAG, "Moving to RiwayatActivity")
        val intent = Intent(this, RiwayatActivity::class.java)
        startActivity(intent)
    }

    private fun moveToCS() {
        Log.d(TAG, "Moving to CsActivity")
        val intent = Intent(this, CsActivity::class.java)
        startActivity(intent)
    }

    private fun moveToInfo() {
        Log.d(TAG, "Moving to InfoActivity")
        val intent = Intent(this, InfoActivity::class.java)
        startActivity(intent)
    }

    companion object {
        const val TAG = "MainActivity"
    }
}
