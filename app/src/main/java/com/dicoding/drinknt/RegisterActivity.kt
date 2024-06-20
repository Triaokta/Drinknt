package com.dicoding.drinknt

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.drinknt.data.DataLogin
import com.dicoding.drinknt.data.DataRegister
import com.dicoding.drinknt.data.SsPreferences
import com.dicoding.drinknt.databinding.ActivityRegisterBinding
import com.dicoding.drinknt.viewmodel.LoginViewModel
import com.dicoding.drinknt.viewmodel.UserViewModel
import com.dicoding.drinknt.viewmodel.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val loginViewModel: LoginViewModel by lazy {
        ViewModelProvider(this)[LoginViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onClicked()

        binding.btnGoogle.setOnClickListener { moveToGoogle() }
        binding.btnFb.setOnClickListener { moveToFb() }

        val ssPref = SsPreferences.getInstance(dataStore)
        val userViewModel = ViewModelProvider(this, ViewModelFactory(ssPref))[UserViewModel::class.java]
        userViewModel.getLogin().observe(this) { sessionTrue ->
            if (sessionTrue) {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        // Gabungkan kedua observer menjadi satu
        loginViewModel.message.observe(this) { msg ->
            registerResponse(loginViewModel.isError, msg)
            loginResponse(loginViewModel.isError, msg, userViewModel)
        }

        loginViewModel.loading.observe(this) {
            onLoading(it)
        }
    }

    private fun moveToGoogle(){
        Log.d(MainActivity.TAG, "Moving to GoogleRegisterActivity")
        val intent = Intent(this, GoogleRegister::class.java)
        startActivity(intent)
    }

    private fun moveToFb(){
        Log.d(MainActivity.TAG, "Moving to FbRegisterActivity")
        val intent = Intent(this, FbRegister::class.java)
        startActivity(intent)
    }

    private fun registerResponse(error: Boolean, msg: String) {
        if (!error) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            val user = DataLogin(
                binding.cvEmail.text.toString(),
                binding.cvPass.text.toString()
            )
            loginViewModel.getLoginResponse(user)

        } else {
            binding.cvEmail.setErrorMsg(resources.getString(R.string.emailTaken), binding.cvEmail.text.toString())
            Toast.makeText(this, resources.getString(R.string.emailTaken), Toast.LENGTH_SHORT).show()
        }
    }


    private fun loginResponse(error: Boolean, msg: String, userViewModel: UserViewModel) {
        if (!error) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            val user = loginViewModel.userLogin.value
            if (user != null && user.loginResult != null) {
                userViewModel.saveLogin(true)
                user.loginResult.token.let { userViewModel.saveToken(it) }
                user.loginResult.name.let { userViewModel.saveName(it) }
            } else {
                Toast.makeText(this, "Login data is null", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }

    private fun onLoading(it: Boolean) {
        binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
    }

    private fun onClicked() {
        binding.seePass.setOnCheckedChangeListener { _, checked ->
            if (checked) {
                binding.cvPass.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.cvSamePass.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                binding.cvPass.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.cvSamePass.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            binding.cvPass.text?.let { binding.cvPass.setSelection(it.length) }
            binding.cvPass.text?.let { binding.cvSamePass.setSelection(it.length) }
        }

        binding.btnRegis.setOnClickListener {
            binding.apply {
                cvName.clearFocus()
                cvEmail.clearFocus()
                cvPass.clearFocus()
                cvSamePass.clearFocus()
            }

            if (binding.cvName.nameValid && binding.cvEmail.emailValid && binding.cvPass.passValid && binding.cvSamePass.samaPassValid) {
                val dataRegisterAccount = DataRegister(
                    name = binding.cvName.text.toString().trim(),
                    email = binding.cvEmail.text.toString().trim(),
                    password = binding.cvPass.text.toString().trim()
                )
                loginViewModel.getRegisterResponse(dataRegisterAccount)
            } else {
                if (!binding.cvName.nameValid) binding.cvName.error = resources.getString(R.string.noName)
                if (!binding.cvEmail.emailValid) binding.cvEmail.error = resources.getString(R.string.noEmail)
                if (!binding.cvPass.passValid) binding.cvPass.error = resources.getString(R.string.noPass)
                if (!binding.cvSamePass.samaPassValid) binding.cvSamePass.error = resources.getString(R.string.noSamePass)
                Toast.makeText(this, "Data yang dimasukkan tidak valid", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
