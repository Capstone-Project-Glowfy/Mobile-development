package com.bangkit.glowfyapp.data.api

import com.bangkit.glowfyapp.data.models.ExampleResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("products")
    fun getProducts(): Call<ExampleResponse>

    @GET("products/category/{category}")
    fun getProductsByCategory(
        @Path("category") category: String
    ): Call<ExampleResponse>

}