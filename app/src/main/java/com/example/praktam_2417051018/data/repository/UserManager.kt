package com.example.praktam_2417051018.data.repository

import android.content.Context
import android.content.SharedPreferences

object UserManager {
    private const val PREFS_NAME = "moodflix_user"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
    private const val KEY_USERNAME = "username"
    private const val KEY_EMAIL = "email"
    private const val KEY_BIO = "bio"
    
    private const val KEY_REG_USERNAME = "reg_username"
    private const val KEY_REG_EMAIL = "reg_email"
    private const val KEY_REG_PASSWORD = "reg_password"
    private const val KEY_PIC_PATH = "pic_path"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        if (!::prefs.isInitialized) {
            prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }
    }

    var isLoggedIn: Boolean
        get() = if (::prefs.isInitialized) prefs.getBoolean(KEY_IS_LOGGED_IN, false) else false
        set(value) {
            if (::prefs.isInitialized) {
                prefs.edit().putBoolean(KEY_IS_LOGGED_IN, value).apply()
            }
        }

    var username: String
        get() = if (::prefs.isInitialized) prefs.getString(KEY_USERNAME, "") ?: "" else ""
        set(value) {
            if (::prefs.isInitialized) {
                prefs.edit().putString(KEY_USERNAME, value).apply()
            }
        }

    var email: String
        get() = if (::prefs.isInitialized) prefs.getString(KEY_EMAIL, "") ?: "" else ""
        set(value) {
            if (::prefs.isInitialized) {
                prefs.edit().putString(KEY_EMAIL, value).apply()
            }
        }

    var bio: String
        get() = if (::prefs.isInitialized) prefs.getString(KEY_BIO, "Penikmat film sejati.") ?: "Penikmat film sejati." else "Penikmat film sejati."
        set(value) {
            if (::prefs.isInitialized) {
                prefs.edit().putString(KEY_BIO, value).apply()
            }
        }

    var registeredUsername: String
        get() = if (::prefs.isInitialized) prefs.getString(KEY_REG_USERNAME, "") ?: "" else ""
        set(value) {
            if (::prefs.isInitialized) {
                prefs.edit().putString(KEY_REG_USERNAME, value).apply()
            }
        }

    var registeredEmail: String
        get() = if (::prefs.isInitialized) prefs.getString(KEY_REG_EMAIL, "") ?: "" else ""
        set(value) {
            if (::prefs.isInitialized) {
                prefs.edit().putString(KEY_REG_EMAIL, value).apply()
            }
        }

    var registeredPassword: String
        get() = if (::prefs.isInitialized) prefs.getString(KEY_REG_PASSWORD, "") ?: "" else ""
        set(value) {
            if (::prefs.isInitialized) {
                prefs.edit().putString(KEY_REG_PASSWORD, value).apply()
            }
        }

    var profilePicturePath: String
        get() = if (::prefs.isInitialized) prefs.getString(KEY_PIC_PATH, "") ?: "" else ""
        set(value) {
            if (::prefs.isInitialized) {
                prefs.edit().putString(KEY_PIC_PATH, value).apply()
            }
        }

    fun logout() {
        if (::prefs.isInitialized) {
            prefs.edit()
                .putBoolean(KEY_IS_LOGGED_IN, false)
                .putString(KEY_USERNAME, "")
                .putString(KEY_EMAIL, "")
                .apply()
        }
    }
}
