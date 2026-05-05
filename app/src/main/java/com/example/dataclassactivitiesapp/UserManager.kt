package com.example.dataclassactivitiesapp

import android.content.Context

class UserManager(context: Context) {

    private val preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    init {
        seedDefaultUser()
    }

    fun registerUser(username: String, password: String): Boolean {
        if (userExists(username)) {
            return false
        }

        preferences.edit()
            .putString(buildPasswordKey(username), password)
            .apply()

        return true
    }

    fun validateUser(username: String, password: String): Boolean {
        val savedPassword = preferences.getString(buildPasswordKey(username), null)
        return savedPassword != null && savedPassword == password
    }

    fun userExists(username: String): Boolean {
        return preferences.contains(buildPasswordKey(username))
    }

    private fun seedDefaultUser() {
        if (!userExists(DEFAULT_USER)) {
            preferences.edit()
                .putString(buildPasswordKey(DEFAULT_USER), DEFAULT_PASSWORD)
                .apply()
        }
    }

    private fun buildPasswordKey(username: String): String {
        return "user_" + username.lowercase().trim()
    }

    companion object {
        private const val PREF_NAME = "users_preferences"
        const val DEFAULT_USER = "admin"
        const val DEFAULT_PASSWORD = "1234"
    }
}