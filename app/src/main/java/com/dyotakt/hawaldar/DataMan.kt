package com.dyotakt.hawaldar

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object DataMan {

    private val PREF_NAME = "MySharedPrefs"
    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun getAllData(): MutableMap<String, *>? {
        return sharedPreferences.all
    }
}