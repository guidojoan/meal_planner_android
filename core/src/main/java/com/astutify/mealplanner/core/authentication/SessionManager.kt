package com.astutify.mealplanner.core.authentication

import android.content.SharedPreferences
import com.astutify.mealplanner.core.Mockable

@Mockable
class SessionManager(private val preferences: SharedPreferences) {

    fun setTokens(token: String, refreshToken: String) {
        preferences.edit()
            .putString(TOKEN, token)
            .putString(REFRESH_TOKEN, refreshToken)
            .apply()
    }

    fun setHasHouse() {
        preferences.edit()
            .putBoolean(HAS_HOUSE, true)
            .apply()
    }

    fun clearHasHouse() {
        val editor = preferences.edit()
        editor.remove(HAS_HOUSE)
        editor.apply()
    }

    fun hasHouse(): Boolean {
        return preferences.getBoolean(HAS_HOUSE, false)
    }

    fun isLogued(): Boolean {
        return preferences.getString(TOKEN, null) != null
    }

    fun getToken(): String {
        return preferences.getString(TOKEN, "")!!
    }

    fun getRefreshToken(): String {
        return preferences.getString(REFRESH_TOKEN, "")!!
    }

    fun logout() {
        val editor = preferences.edit()
        editor.remove(TOKEN)
        editor.remove(REFRESH_TOKEN)
        editor.apply()
    }

    companion object {
        const val TOKEN = "T"
        const val REFRESH_TOKEN = "RT"
        const val HAS_HOUSE = "hasHouse"
    }
}
