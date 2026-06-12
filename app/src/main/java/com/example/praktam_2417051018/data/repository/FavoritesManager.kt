package com.example.praktam_2417051018.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateListOf

object FavoritesManager {
    private const val PREFS_NAME = "moodflix_favorites"
    private const val KEY_FAVORITES = "favorites_list"
    
    private lateinit var prefs: SharedPreferences
    val favoriteTitles = mutableStateListOf<String>()

    fun init(context: Context) {
        if (!::prefs.isInitialized) {
            prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val savedFavorites = prefs.getStringSet(KEY_FAVORITES, emptySet()) ?: emptySet()
            favoriteTitles.clear()
            favoriteTitles.addAll(savedFavorites)
        }
    }

    fun isFavorite(title: String): Boolean {
        return favoriteTitles.contains(title)
    }

    fun toggleFavorite(title: String) {
        if (favoriteTitles.contains(title)) {
            favoriteTitles.remove(title)
        } else {
            favoriteTitles.add(title)
        }
        saveFavorites()
    }

    private fun saveFavorites() {
        if (::prefs.isInitialized) {
            prefs.edit().putStringSet(KEY_FAVORITES, favoriteTitles.toSet()).apply()
        }
    }

    var savedLevel: String?
        get() = if (::prefs.isInitialized) prefs.getString("saved_level", null) else null
        set(value) {
            if (::prefs.isInitialized) {
                prefs.edit().putString("saved_level", value).apply()
            }
        }

    fun getSavedLevelForGenre(genre: String): String? {
        return if (::prefs.isInitialized) prefs.getString("saved_level_${genre.lowercase()}", null) else null
    }

    fun saveLevelForGenre(genre: String, level: String?) {
        if (::prefs.isInitialized) {
            prefs.edit().putString("saved_level_${genre.lowercase()}", level).apply()
        }
    }
}
