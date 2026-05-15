package com.example.praktam_2417051018.data.model

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("title") val title: String,
    @SerializedName("year") val year: String,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("description") val description: String,
    @SerializedName("genre") val genre: String,
    @SerializedName("youtube_url") val youtubeUrl: String? = null,
    @SerializedName("level") val level: String? = null  // "ringan", "sedang", "ekstrem"
)