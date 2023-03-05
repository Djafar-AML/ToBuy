package com.example.tobuy.prefs

import android.content.Context
import android.content.SharedPreferences
import com.example.tobuy.BuildConfig

private const val PREF_PRIORITY_HIGH = "pref_priority_high"
private const val PREF_PRIORITY_MEDIUM = "pref_priority_medium"
private const val PREF_PRIORITY_LOW = "pref_priority_low"

private const val DEFAULT_PRIORITY_HIGH = -3342336
private const val DEFAULT_PRIORITY_MEDIUM = -3315456
private const val DEFAULT_PRIORITY_LOW = -10107038

object Prefs {

    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {

        if (!Prefs::preferences.isInitialized) {
            preferences = context.getSharedPreferences(
                "${BuildConfig.APPLICATION_ID}.ToBuyPref",
                Context.MODE_PRIVATE
            )
        }

    }

    fun getHighPriorityColor(): Int = getInt(PREF_PRIORITY_HIGH, DEFAULT_PRIORITY_HIGH)
    fun getMediumPriorityColor(): Int = getInt(PREF_PRIORITY_MEDIUM, DEFAULT_PRIORITY_MEDIUM)
    fun getLowPriorityColor(): Int = getInt(PREF_PRIORITY_LOW, DEFAULT_PRIORITY_LOW)

    fun setHighPriorityColor(color: Int) = setInt(PREF_PRIORITY_HIGH, color)
    fun setMediumPriorityColor(color: Int) = setInt(PREF_PRIORITY_MEDIUM, color)
    fun setLowPriorityColor(color: Int) = setInt(PREF_PRIORITY_LOW, color)

    fun clear() {
        throwExceptionIfPrefsNotInitialized()
        preferences.edit().clear().apply()
    }

    private fun setInt(name: String, value: Int) {
        preferences.edit().putInt(name, value).apply()
    }

    private fun getInt(name: String, defaultValue: Int = 0): Int {
        return preferences.getInt(name, defaultValue)
    }

    private fun throwExceptionIfPrefsNotInitialized() {
        if (!Prefs::preferences.isInitialized)
            throw Exception("First initialize the prefs object!")
    }

}