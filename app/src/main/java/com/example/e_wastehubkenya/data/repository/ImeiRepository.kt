package com.example.e_wastehubkenya.data.repository

import com.example.e_wastehubkenya.data.remote.ImeiApiService
import com.example.e_wastehubkenya.data.remote.RetrofitInstance

class ImeiRepository(private val apiService: ImeiApiService = RetrofitInstance.imeiApi) {

    suspend fun verifyImei(imei: String, apiKey: String) = apiService.checkImei(imei, apiKey)

}
