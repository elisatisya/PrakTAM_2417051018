package com.example.praktam_2417051018.data.repository

import com.example.praktam_2417051018.data.api.RetrofitClient
import com.example.praktam_2417051018.data.model.Movie
import com.example.praktam_2417051018.data.model.LocalMovieSource

class MovieRepository {
    private val apiService = RetrofitClient.instance

    suspend fun getMovies(): List<Movie> {
        return try {
            apiService.getMovies()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getAllMovies(): List<Movie> {
        val remote = getMovies()
        val local = LocalMovieSource.movies
        return (remote + local).distinctBy { it.title }
    }

    suspend fun getMoviesByGenre(genre: String): List<Movie> {
        val all = getAllMovies()
        return if (genre.equals("Horror", ignoreCase = true)) {
            all.filter { it.genre.equals("Supernatural", ignoreCase = true) || it.genre.equals("Psychological", ignoreCase = true) }
        } else {
            all.filter { it.genre.equals(genre, ignoreCase = true) }
        }
    }
}