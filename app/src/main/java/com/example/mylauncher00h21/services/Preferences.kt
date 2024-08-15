package com.example.mylauncher00h21.services

import android.content.Context

class Preferences {
    companion object {
        private const val PREFERENCE_NAME: String = "MyPrefs"

        fun savePreferences(context: Context, key: String, value: String) {
            val sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(key, value)
            editor.apply()
        }

        fun loadPreferences(context: Context, key: String): String? {
            val sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString(key, null)
        }


    }
}