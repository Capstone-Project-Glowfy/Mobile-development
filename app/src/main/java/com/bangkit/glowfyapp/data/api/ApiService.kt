package com.bangkit.glowfyapp.data.api

import com.bangkit.glowfyapp.data.models.ExampleResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("products")
    fun getProducts(): Call<ExampleResponse>

}