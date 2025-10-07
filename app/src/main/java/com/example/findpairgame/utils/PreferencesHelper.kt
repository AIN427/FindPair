package com.example.findpairgame.utils

import android.content.Context
import android.content.SharedPreferences

object PreferencesHelper {
    private const val PREFS_NAME = "find_pair_game_prefs"
    private const val KEY_MUSIC_ENABLED = "music_enabled"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun isMusicEnabled(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_MUSIC_ENABLED, true)
    }

    fun setMusicEnabled(context: Context, enabled: Boolean) {
        getPreferences(context).edit().putBoolean(KEY_MUSIC_ENABLED, enabled).apply()
    }
}
