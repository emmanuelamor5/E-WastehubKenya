package com.example.e_wastehubkenya.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_wastehubkenya.data.remote.ImeiVerificationResponse
import com.example.e_wastehubkenya.data.repository.ImeiRepository
import kotlinx.coroutines.launch

class ImeiVerificationViewModel(private val repository: ImeiRepository = ImeiRepository()) : ViewModel() {

    private val _verificationResult = MutableLiveData<ImeiVerificationResponse>()
    val verificationResult: LiveData<ImeiVerificationResponse> = _verificationResult

    fun verifyImei(imei: String, apiKey: String) {
        viewModelScope.launch {
            val response = repository.verifyImei(imei, apiKey)
            _verificationResult.postValue(response.body())
        }
    }
}
