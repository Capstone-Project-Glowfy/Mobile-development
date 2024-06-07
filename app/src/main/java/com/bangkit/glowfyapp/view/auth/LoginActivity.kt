package com.bangkit.glowfyapp.view.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bangkit.glowfyapp.R
import com.bangkit.glowfyapp.data.models.ResultApi
import com.bangkit.glowfyapp.data.models.auth.LoginResponse
import com.bangkit.glowfyapp.databinding.ActivityLoginBinding
import com.bangkit.glowfyapp.utils.ViewModelFactory
import com.bangkit.glowfyapp.view.home.HomeActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val viewModel by viewModels<AuthViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupAction()
    }

    private fun setupAction() {
        with(binding) {
            loginButton.setOnClickListener { loginHandler() }
            toRegisterButton.setOnClickListener { navigateToRegister() }
        }
    }

    private fun navigateToRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    private fun loginHandler() {
        val email = binding.emailEt.text.toString()
        val password = binding.passwordEt.text.toString()
        when {
            email.isEmpty() -> {
                binding.usernameLayout.error = getString(R.string.message_validation)
            }
            password.isEmpty() -> {
                binding.passwordLayout.error = getString(R.string.message_validation)
            }
            else -> {
                viewModel.loginUser(email, password).observe(this@LoginActivity) { response ->
                    when(response) {
                        is ResultApi.Loading -> {
                            showLoading(true)
                        }
                        is ResultApi.Success -> {
                            showLoading(false)
                            loginProcess(response.data)
                            moveActivity()
                        }
                        is ResultApi.Error -> {
                            showLoading(false)
                            showToast(response.error)
                        }
                    }
                }
            }
        }
    }

    private fun loginProcess(data : LoginResponse){
        viewModel.saveSession(data.loginResult)
        showToast(getString(R.string.login_success_message))
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun moveActivity(){
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}