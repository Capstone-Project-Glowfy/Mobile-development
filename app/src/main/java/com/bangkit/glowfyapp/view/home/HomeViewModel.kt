package com.bangkit.glowfyapp.view.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.glowfyapp.data.historydatabase.ProfileEntity
import com.bangkit.glowfyapp.data.models.ResultApi
import com.bangkit.glowfyapp.data.models.auth.LoginResult
import com.bangkit.glowfyapp.data.models.response.ArticlesResponse
import com.bangkit.glowfyapp.data.models.response.ProductResponse
import com.bangkit.glowfyapp.data.models.response.ProfileResponse
import com.bangkit.glowfyapp.data.models.response.SkinsResponse
import com.bangkit.glowfyapp.data.repository.DataRepository
import kotlinx.coroutines.launch
import java.io.File

class HomeViewModel(private val repository: DataRepository) : ViewModel() {

    private val _profile = MutableLiveData<ResultApi<ProfileResponse>>()
    val profile: LiveData<ResultApi<ProfileResponse>> = _profile

    private val _dbProfile = MutableLiveData<ProfileEntity?>()
    val dbProfile: LiveData<ProfileEntity?> = _dbProfile

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

    fun updateProfile(token: String, id: String, img: File) {
        viewModelScope.launch {
            repository.profileUpdate(token, id, img).observeForever{
                _profile.value = it
            }
        }
    }

    fun getProfile() {
        viewModelScope.launch {
            val profileImg = repository.getProfile()
            _dbProfile.postValue(profileImg)
        }
    }

//    fun saveProfile(profile: ProfileEntity) {
//        viewModelScope.launch {
//            repository.saveProfile(profile)
//        }
//    }

    fun saveProfile(profile: ProfileEntity) {
        viewModelScope.launch {
            try {
                repository.saveProfile(profile)
                getProfile()
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error saving profile", e)
            }
        }
    }
}