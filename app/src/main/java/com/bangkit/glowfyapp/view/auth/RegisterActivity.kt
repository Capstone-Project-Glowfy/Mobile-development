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
import com.bangkit.glowfyapp.databinding.ActivityRegisterBinding
import com.bangkit.glowfyapp.utils.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val viewModel by viewModels<AuthViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
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
            registerButton.setOnClickListener { registerHandler() }
            toLoginButton.setOnClickListener { navigateToLogin() }
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun registerHandler() {
        val name  = binding.username.text.toString()
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()
        when {
            name.isEmpty() -> {
                binding.usernameLayout.error = getString(R.string.message_validation)
            }
            email.isEmpty() -> {
                binding.emailLayout.error = getString(R.string.message_validation)
            }
            password.isEmpty() -> {
                binding.passwordLayout.error = getString(R.string.message_validation)
            }
            else -> {
                viewModel.registerUser(name, email, password).observe(this@RegisterActivity) { response ->
                    when(response) {
                        is ResultApi.Loading -> {
                            showLoading(true)
                        }
                        is ResultApi.Success -> {
                            showLoading(false)
                            showToast(getString(R.string.register_success_message))
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun moveActivity(){
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}