package com.bangkit.glowfyapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import com.bangkit.glowfyapp.data.repository.UserPreference
import com.bangkit.glowfyapp.data.repository.dataStore
import com.bangkit.glowfyapp.view.auth.LoginActivity
import com.bangkit.glowfyapp.view.home.HomeActivity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private lateinit var userPreference: UserPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        userPreference = UserPreference.getInstance(applicationContext.dataStore)

        MainScope().launch {
            userPreference.getUser().asLiveData().observe(this@SplashActivity) { user ->
                if (user.isLogin) {
                    navigateToMain()
                } else {
                    navigateToLogin()
                }
            }
        }
    }
    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
    private fun navigateToMain() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}