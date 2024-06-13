package com.bangkit.glowfyapp.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.glowfyapp.data.api.ApiConfig
import com.bangkit.glowfyapp.data.api.ApiService
import com.bangkit.glowfyapp.data.historydatabase.ScanHistory
import com.bangkit.glowfyapp.data.historydatabase.ScanHistoryDao
import com.bangkit.glowfyapp.data.models.ErrorResponse
import com.bangkit.glowfyapp.data.models.ResultApi
import com.bangkit.glowfyapp.data.models.auth.LoginResponse
import com.bangkit.glowfyapp.data.models.auth.LoginResult
import com.bangkit.glowfyapp.data.models.auth.RegisterResponse
import com.bangkit.glowfyapp.data.models.response.ArticlesResponse
import com.bangkit.glowfyapp.data.models.response.ProductResponse
import com.bangkit.glowfyapp.data.models.response.ScanResponse
import com.bangkit.glowfyapp.data.models.response.SkinsResponse
import com.bangkit.glowfyapp.utils.Utility
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File

class DataRepository(
    private val apiService: ApiService,
    private val pref: UserPreference,
    private val context: Context,
    private val scanHistoryDao: ScanHistoryDao
) {
    suspend fun saveSession(user: LoginResult) {
        pref.saveSession(user)
    }

    fun getUser(): Flow<LoginResult> {
        return pref.getUser()
    }

    suspend fun logout() {
        pref.logout()
    }

    fun registerUser(name: String, email: String, password: String): LiveData<ResultApi<RegisterResponse>> = liveData {
        emit(ResultApi.Loading)
        if (!Utility.isNetworkAvailable(context)) {
            emit(ResultApi.Error("No network available"))
            return@liveData
        }
        try {
            val response = apiService.userRegister(name, email, password)
            emit(ResultApi.Success(response))
        } catch (e: HttpException) {
            emit(handleHttpException(e))
        }
    }

    fun loginUser(email: String, password: String): LiveData<ResultApi<LoginResponse>> = liveData {
        emit(ResultApi.Loading)
        if (!Utility.isNetworkAvailable(context)) {
            emit(ResultApi.Error("No network available"))
            return@liveData
        }
        try {
            val response = apiService.userLogin(email, password)
            emit(ResultApi.Success(response))
        } catch (e: HttpException) {
            emit(handleHttpException(e))
        }
    }

    fun getArticles(token: String): LiveData<ResultApi<ArticlesResponse>> = liveData {
        emit(ResultApi.Loading)
        if (!Utility.isNetworkAvailable(context)) {
            emit(ResultApi.Error("No network available"))
            return@liveData
        }
        try {
            val response = ApiConfig().getApiService(token).getArticles()
            emit(ResultApi.Success(response))
        } catch (e: HttpException) {
            emit(handleHttpException(e))
        }
    }

    fun getSkins(token: String): LiveData<ResultApi<SkinsResponse>> = liveData {
        emit(ResultApi.Loading)
        if (!Utility.isNetworkAvailable(context)) {
            emit(ResultApi.Error("No network available"))
            return@liveData
        }
        try {
            val response = ApiConfig().getApiService(token).getSkins()
            emit(ResultApi.Success(response))
        } catch (e: HttpException) {
            emit(handleHttpException(e))
        }
    }

    fun getProducts(token: String): LiveData<ResultApi<ProductResponse>> = liveData {
        emit(ResultApi.Loading)
        if (!Utility.isNetworkAvailable(context)) {
            emit(ResultApi.Error("No network available"))
            return@liveData
        }
        try {
            val response = ApiConfig().getApiService(token).getProducts()
            emit(ResultApi.Success(response))
        } catch (e: HttpException) {
            emit(handleHttpException(e))
        }
    }

    fun getProductsByCategory(token: String, category: String): LiveData<ResultApi<ProductResponse>> = liveData {
        emit(ResultApi.Loading)
        if (!Utility.isNetworkAvailable(context)) {
            emit(ResultApi.Error("No network available"))
            return@liveData
        }
        try {
            val response = ApiConfig().getApiService(token).getProductsByCategory(category)
            emit(ResultApi.Success(response))
        } catch (e: HttpException) {
            emit(handleHttpException(e))
        }
    }

    fun faceDetection(token: String, imageFile: File): LiveData<ResultApi<ScanResponse>> = liveData {
        emit(ResultApi.Loading)
        val requestImageFile = imageFile.asRequestBody("image/jpg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "image",
            imageFile.name,
            requestImageFile
        )
        try {
            val response = ApiConfig().getApiService(token).faceDetection(multipartBody)
            emit(ResultApi.Success(response))
        } catch (e: HttpException) {
            emit(handleHttpException(e))
        }
    }

    suspend fun addScanToHistory(scanHistory: ScanHistory) {
        scanHistoryDao.addScanToHistory(scanHistory)
    }

    suspend fun getScanHistory(): List<ScanHistory> {
        return scanHistoryDao.getScanHistory()
    }

    suspend fun deleteScanHistory(id: Int) {
        scanHistoryDao.clearScanHistory(id)
    }

    private fun handleHttpException(e: HttpException): ResultApi.Error {
        val jsonInString = e.response()?.errorBody()?.string()
        Log.e("DataRepository", "Error response: $jsonInString")
        val errorMessage = try {
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            errorBody.message
        } catch (ex: JsonSyntaxException) {
            jsonInString
        } catch (ex: Exception) {
            null
        }
        val errorText= "An error occurred"
        return ResultApi.Error(errorMessage ?: errorText)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: DataRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference,
            context: Context,
            scanHistoryDao: ScanHistoryDao
        ): DataRepository =
            instance ?: synchronized(this) {
                instance ?: DataRepository(apiService, userPreference, context, scanHistoryDao)
            }.also { instance = it }
    }
}