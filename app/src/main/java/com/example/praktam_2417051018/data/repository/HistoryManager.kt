package com.example.praktam_2417051018.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateListOf

object HistoryManager {
    private const val PREFS_NAME = "moodflix_history"
    private const val KEY_HISTORY = "history_list"
    
    private lateinit var prefs: SharedPreferences
    val historyTitles = mutableStateListOf<String>()

    fun init(context: Context) {
        if (!::prefs.isInitialized) {
            prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val savedHistory = prefs.getString(KEY_HISTORY, "") ?: ""
            historyTitles.clear()
            if (savedHistory.isNotEmpty()) {
                historyTitles.addAll(savedHistory.split("|||"))
            }
        }
    }

    fun isWatched(title: String): Boolean {
        return historyTitles.contains(title)
    }

    fun addWatched(title: String) {
        // Remove duplicate so it moves to the end (newest) when re-added
        historyTitles.remove(title)
        historyTitles.add(title)
        saveHistory()
    }

    fun clearHistory() {
        historyTitles.clear()
        saveHistory()
    }

    private fun saveHistory() {
        if (::prefs.isInitialized) {
            val serialized = historyTitles.joinToString("|||")
            prefs.edit().putString(KEY_HISTORY, serialized).apply()
        }
    }
}
