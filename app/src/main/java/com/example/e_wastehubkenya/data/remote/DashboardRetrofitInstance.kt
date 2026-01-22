package com.example.e_wastehubkenya.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DashboardRetrofitInstance {

    private const val BASE_URL = "https://dash.imei.info/"

    val api: DashboardImeiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DashboardImeiApiService::class.java)
    }
}
