package com.bangkit.glowfyapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import com.bangkit.glowfyapp.data.repository.UserPreference
import com.bangkit.glowfyapp.data.repository.dataStore
import com.bangkit.glowfyapp.view.home.HomeActivity
import com.bangkit.glowfyapp.view.welcome.AuthActivity
import com.bangkit.glowfyapp.view.welcome.WelcomeActivity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private lateinit var userPreference: UserPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        userPreference = UserPreference.getInstance(applicationContext.dataStore)

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val isFirstRun = sharedPreferences.getBoolean("isFirstRun", true)

        if (isFirstRun) {
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()

            val editor = sharedPreferences.edit()
            editor.putBoolean("isFirstRun", false)
            editor.apply()
        } else {
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
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }

    private fun navigateToMain() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}