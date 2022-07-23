package eu.davidknotek.pomotodo.util

import android.content.Context

class DataSavedPomo(val context: Context) {
    var date: String = ""
    var pomodoroCount: Int = 0

    companion object {
        private const val FILE_NAME = "data_pomodoro"
        private const val DATE = "date"
        private const val POMODORO_COUNT = "pomodoro_count"
    }

    init {
        load()
    }

    fun save() {
        val pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putInt(POMODORO_COUNT, pomodoroCount)
        editor.putString(DATE, date)
        editor.apply()
    }

    fun load() {
        val pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        pomodoroCount = pref.getInt(POMODORO_COUNT, 0)
        date = pref.getString(DATE, "").toString()
    }
}