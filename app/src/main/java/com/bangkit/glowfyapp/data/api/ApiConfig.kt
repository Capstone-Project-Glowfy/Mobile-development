package com.bangkit.glowfyapp.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
        fun getApiService() : ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiService::class.java)
        }

    companion object {
        const val BASE_URL = "https://dummyjson.com/"
    }
}