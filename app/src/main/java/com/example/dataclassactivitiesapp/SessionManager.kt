package com.example.dataclassactivitiesapp

import android.content.Context

class SessionManager(context: Context) {

    private val preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun login(username: String) {
        preferences.edit()
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .putString(KEY_LOGGED_USER, username)
            .apply()
    }

    fun logout() {
        preferences.edit()
            .putBoolean(KEY_IS_LOGGED_IN, false)
            .remove(KEY_LOGGED_USER)
            .apply()
    }

    fun isLoggedIn(): Boolean = preferences.getBoolean(KEY_IS_LOGGED_IN, false)

    fun getLoggedUser(): String = preferences.getString(KEY_LOGGED_USER, "") ?: ""

    companion object {
        private const val PREF_NAME = "session_preferences"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_LOGGED_USER = "logged_user"
    }
}