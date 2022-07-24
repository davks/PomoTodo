package eu.davidknotek.pomotodo.util

import android.content.Context
import java.time.LocalDateTime

class StoredData(val context: Context) {
    var dateTime: String = ""
    var pomodoroCount: Int = 0

    companion object {
        private const val FILE_NAME = "data_pomodoro"
        private const val DATE_TIME = "date_time"
        private const val POMODORO_COUNT = "pomodoro_count"
    }

    init {
        load()
    }

    fun save() {
        val pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putInt(POMODORO_COUNT, pomodoroCount)
        editor.putString(DATE_TIME, dateTime)
        editor.apply()
        load()
    }

    private fun load() {
        val pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        pomodoroCount = pref.getInt(POMODORO_COUNT, 0)
        dateTime = pref.getString(DATE_TIME, LocalDateTime.now().toString()).toString()
    }
}