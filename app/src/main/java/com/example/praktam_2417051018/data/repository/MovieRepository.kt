package com.example.praktam_2417051018.data.repository

import com.example.praktam_2417051018.data.api.RetrofitClient
import com.example.praktam_2417051018.data.model.Movie

class MovieRepository {
    private val apiService = RetrofitClient.instance

    suspend fun getMovies(): List<Movie> {
        return try {
            apiService.getMovies()
        } catch (e: Exception) {
            emptyList()
        }
    }
}