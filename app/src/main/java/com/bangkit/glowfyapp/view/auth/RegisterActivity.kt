package com.bangkit.glowfyapp.view.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bangkit.glowfyapp.R
import com.bangkit.glowfyapp.data.models.ResultApi
import com.bangkit.glowfyapp.databinding.ActivityRegisterBinding
import com.bangkit.glowfyapp.utils.ViewModelFactory
import com.bangkit.glowfyapp.view.customview.CustomAlertDialog

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
            registerBtn.setOnClickListener { registerHandler() }
            toLoginText.setOnClickListener { navigateToLogin() }
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun registerHandler() {
        val name  = binding.usernameEt.text.toString()
        val email = binding.emailEt.text.toString()
        val password = binding.passEt.text.toString()
        when {
            name.isEmpty() -> {
                binding.usernameEtLayout.error = getString(R.string.message_validation)
            }
            email.isEmpty() -> {
                binding.usernameEtLayout.error = getString(R.string.message_validation)
            }
            password.isEmpty() -> {
                binding.usernameEtLayout.error = getString(R.string.message_validation)
            }
            else -> {
                viewModel.registerUser(name, email, password).observe(this@RegisterActivity) { response ->
                    when(response) {
                        is ResultApi.Loading -> {
                            showLoading(true)
                        }
                        is ResultApi.Success -> {
                            showLoading(false)
                            successRegisterHandler()
                        }
                        is ResultApi.Error -> {
                            showLoading(false)
                            errorRegisterHandler()
                        }
                    }
                }
            }
        }
    }

    private fun errorRegisterHandler() {
        CustomAlertDialog(this, R.raw.animation_error, R.string.register_error_message, R.string.done).show()
    }

    private fun successRegisterHandler() {
        val dialog = CustomAlertDialog(this, R.raw.animation_yeay_success, R.string.successRegisterMessage, R.string.continueLogin)
        dialog.setOnDismissListener {
            moveActivity()
        }
        dialog.show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingFrame.root.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun moveActivity(){
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}