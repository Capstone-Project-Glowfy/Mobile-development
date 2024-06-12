package com.bangkit.glowfyapp.view.welcome

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bangkit.glowfyapp.R
import com.bangkit.glowfyapp.databinding.ActivityAuthBinding
import com.bangkit.glowfyapp.view.auth.LoginActivity
import com.bangkit.glowfyapp.view.auth.RegisterActivity

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupViewAndAction()
    }

    private fun setupViewAndAction() {
        loginTextFormat()
        navigateToRegister()
    }

    private fun navigateToRegister() {
        binding.registerBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginTextFormat() {
        val fullText = getString(R.string.loginDesc)
        val spannableString = SpannableString(fullText)

        val start = fullText.indexOf("Login")
        val end = start + "Login".length

        val customColor = ContextCompat.getColor(this, R.color.darkGreen)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                navigateToLogin()
            }
        }

        spannableString.setSpan(
            clickableSpan,
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannableString.setSpan(
            ForegroundColorSpan(customColor),
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.loginDesc.text = spannableString
        binding.loginDesc.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}