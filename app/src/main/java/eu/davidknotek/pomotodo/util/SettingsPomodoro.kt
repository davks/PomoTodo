package eu.davidknotek.pomotodo.util

import android.content.Context

class SettingsPomodoro(val context: Context) {
    var workDuration: Int = 25
    var breakDuration: Int = 5
    var breakContinues = false

    companion object {
        private const val FILE_NAME = "pomodor_settings"
        private const val WORK_DURATION = "work_duration"
        private const val BREAK_DURATION = "break_duration"
        private const val BREAK_CONTINUES = "break_continues"
    }

    fun save() {
        val pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putInt(WORK_DURATION, workDuration)
        editor.putInt(BREAK_DURATION, breakDuration)
        editor.putBoolean(BREAK_CONTINUES, breakContinues)
        editor.apply()
    }

    fun load() {
        val pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        workDuration = pref.getInt(WORK_DURATION, 25)
        breakDuration = pref.getInt(BREAK_DURATION, 5)
        breakContinues = pref.getBoolean(BREAK_CONTINUES, false)
    }
}