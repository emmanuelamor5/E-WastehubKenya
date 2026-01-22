package com.example.e_wastehubkenya.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DashboardImeiApiService {
    @GET("api/check/{serialNumber}")
    suspend fun checkImei(
        @Path("serialNumber") serialNumber: String,
        @Query("API_KEY") apiKey: String
    ): Response<DashboardImeiResponse>
}

data class DashboardImeiResponse(
    val success: Boolean
)
