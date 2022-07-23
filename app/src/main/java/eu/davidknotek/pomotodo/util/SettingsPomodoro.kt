package eu.davidknotek.pomotodo.util

import android.content.Context

class SettingsPomodoro(val context: Context) {
    var workDuration: Int = 25
    var shortBreakDuration: Int = 5
    var longBreakDuration: Int = 15
    var breakContinues = false
    var workContinues = false

    init {
        load()
    }

    companion object {
        private const val FILE_NAME = "pomodoro_settings"
        private const val WORK_DURATION = "work_duration"
        private const val SHORT_BREAK_DURATION = "short_break_duration"
        private const val LONG_BREAK_DURATION = "long_break_duration"
        private const val BREAK_CONTINUES = "break_continues"
        private const val WORK_CONTINUES = "work_continues"
    }

    fun save() {
        val pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putInt(WORK_DURATION, workDuration)
        editor.putInt(SHORT_BREAK_DURATION, shortBreakDuration)
        editor.putInt(LONG_BREAK_DURATION, longBreakDuration)
        editor.putBoolean(BREAK_CONTINUES, breakContinues)
        editor.putBoolean(WORK_CONTINUES, workContinues)
        editor.apply()
    }

    fun load() {
        val pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        workDuration = pref.getInt(WORK_DURATION, 25)
        shortBreakDuration = pref.getInt(SHORT_BREAK_DURATION, 5)
        longBreakDuration = pref.getInt(LONG_BREAK_DURATION, 15)
        breakContinues = pref.getBoolean(BREAK_CONTINUES, false)
        workContinues = pref.getBoolean(WORK_CONTINUES, false)
    }
}