package com.bangkit.glowfyapp.utils

import android.content.Context
import com.bangkit.glowfyapp.data.api.ApiConfig
import com.bangkit.glowfyapp.data.repository.DataRepository
import com.bangkit.glowfyapp.data.repository.UserPreference
import com.bangkit.glowfyapp.data.repository.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): DataRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getUser().first() }
        val apiService = ApiConfig().getApiService(user.token)
        return DataRepository.getInstance(apiService, pref)
    }
}