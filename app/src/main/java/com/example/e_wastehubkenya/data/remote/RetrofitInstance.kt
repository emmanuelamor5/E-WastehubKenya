package com.example.e_wastehubkenya.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val IMEI_API_BASE_URL = "https://imei-api.com/" // Replace with your actual base URL

    val imeiApi: ImeiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(IMEI_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ImeiApiService::class.java)
    }
}
