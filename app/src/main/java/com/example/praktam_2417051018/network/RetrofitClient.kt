package com.example.praktam_2417051018.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL =
        "https://gist.githubusercontent.com/elisatisya/ac7a113e2199e1dae76e4b8c17640a85/raw/980d558bbee7f1ac2137dd278a2e09d0037097d9/movies.json"

    val instance: ApiService by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}