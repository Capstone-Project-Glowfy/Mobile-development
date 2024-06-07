package com.bangkit.glowfyapp.view.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.glowfyapp.data.models.ResultApi
import com.bangkit.glowfyapp.data.models.auth.LoginResponse
import com.bangkit.glowfyapp.data.models.auth.LoginResult
import com.bangkit.glowfyapp.data.models.auth.RegisterResponse
import com.bangkit.glowfyapp.data.repository.DataRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: DataRepository) : ViewModel() {
    fun saveSession(user: LoginResult) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
    fun loginUser(email: String, password: String): LiveData<ResultApi<LoginResponse>> {
        return repository.loginUser(email, password)
    }
    fun registerUser(name: String, email: String, password: String): LiveData<ResultApi<RegisterResponse>> {
        return repository.registerUser(name, email, password)
    }
}