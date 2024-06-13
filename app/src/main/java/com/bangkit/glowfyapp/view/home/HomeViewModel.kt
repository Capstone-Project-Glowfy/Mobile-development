package com.bangkit.glowfyapp.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.glowfyapp.data.models.ResultApi
import com.bangkit.glowfyapp.data.models.auth.LoginResult
import com.bangkit.glowfyapp.data.models.response.ArticlesResponse
import com.bangkit.glowfyapp.data.models.response.ProductResponse
import com.bangkit.glowfyapp.data.models.response.SkinsResponse
import com.bangkit.glowfyapp.data.repository.DataRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: DataRepository) : ViewModel() {
    fun getSession(): LiveData<LoginResult> {
        return repository.getUser().asLiveData()
    }
    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
    fun getProducts(token: String): LiveData<ResultApi<ProductResponse>>{
        return repository.getProducts(token)
    }
    fun getSkins(token: String): LiveData<ResultApi<SkinsResponse>>{
        return repository.getSkins(token)
    }
    fun getArticles(token: String): LiveData<ResultApi<ArticlesResponse>>{
        return repository.getArticles(token)
    }

    fun getProductsByCategory(token: String, category: String): LiveData<ResultApi<ProductResponse>>{
        return repository.getProductsByCategory(token, category)
    }
}