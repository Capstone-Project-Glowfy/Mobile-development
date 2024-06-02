package com.bangkit.glowfyapp.view.home.fragments.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.glowfyapp.data.api.ApiConfig
import com.bangkit.glowfyapp.data.models.ExampleResponse
import com.bangkit.glowfyapp.data.models.ProductsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductViewModel : ViewModel() {

    private val _products = MutableLiveData<List<ProductsItem>>()
    val products: LiveData<List<ProductsItem>> = _products

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun fetchProductByCategory(category: String) {
        _isLoading.value = true
        val client = ApiConfig().getApiService().getProductsByCategory(category)
        client.enqueue(object : Callback<ExampleResponse> {
            override fun onResponse(p0: Call<ExampleResponse>, p1: Response<ExampleResponse>) {
                _isLoading.value = false
                if (p1.isSuccessful && p1.body() != null) {
                    _products.value = p1.body()!!.products
                } else {
                    _error.value = "Failed to load products: ${p1.message()}"
                }
            }

            override fun onFailure(p0: Call<ExampleResponse>, p1: Throwable) {
                _isLoading.value = false
                _error.value = "Failed to load products: ${p1.message}"
            }
        })
    }
}