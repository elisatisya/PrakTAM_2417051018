package com.example.praktam_2417051018.network

import com.example.praktam_2417051018.model.Movie
import retrofit2.http.GET

interface ApiService {

    @GET("movies.json")
    suspend fun getMovies(): List<Movie>
}