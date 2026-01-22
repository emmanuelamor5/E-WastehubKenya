package com.example.e_wastehubkenya.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ImeiApiService {
    @GET("check/{imei}")
    suspend fun checkImei(
        @Path("imei") imei: String,
        @Query("API_KEY") apiKey: String,
        @Query("format") format: String = "json"
    ): Response<ImeiVerificationResponse>
}

data class ImeiVerificationResponse(
    val success: Boolean,
    val data: DeviceData?,
    val message: String?
)

data class DeviceData(
    val name: String,
    val brand: String,
    val model: String
)
