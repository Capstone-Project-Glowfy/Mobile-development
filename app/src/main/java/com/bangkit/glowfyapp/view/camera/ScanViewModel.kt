package com.bangkit.glowfyapp.view.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.glowfyapp.data.models.ResultApi
import com.bangkit.glowfyapp.data.models.auth.LoginResult
import com.bangkit.glowfyapp.data.models.items.ScanResponse
import com.bangkit.glowfyapp.data.repository.DataRepository
import kotlinx.coroutines.launch
import java.io.File

class ScanViewModel(private val repository: DataRepository): ViewModel() {

    private val _scanResult = MutableLiveData<ResultApi<ScanResponse>>()
    val scanResult: LiveData<ResultApi<ScanResponse>> = _scanResult

    fun getSession(): LiveData<LoginResult> {
        return repository.getUser().asLiveData()
    }

    fun faceDetection(token: String, file: File) {
        viewModelScope.launch {
            repository.faceDetection(token, file).observeForever{
                _scanResult.value = it
            }
        }
    }
}