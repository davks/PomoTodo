package eu.davidknotek.pomotodo.data.viewmodels

import android.app.Application
import android.media.MediaPlayer
import android.net.Uri
import android.os.CountDownTimer
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import eu.davidknotek.pomotodo.R
import eu.davidknotek.pomotodo.data.TaskDatabase
import eu.davidknotek.pomotodo.data.models.TaskEntity
import eu.davidknotek.pomotodo.data.repository.TaskRepository
import eu.davidknotek.pomotodo.data.repository.TaskRepositoryImpl
import eu.davidknotek.pomotodo.util.StoredData
import eu.davidknotek.pomotodo.util.SettingsPomodoro
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class PomodoroViewModel(application: Application, var taskEntity: TaskEntity) :
    AndroidViewModel(application) {
    private val app = application
    private val taskDao = TaskDatabase.getDatabase(application).taskDao()
    private val repository: TaskRepository
    private val settingsPomodoro = SettingsPomodoro(application.applicationContext)
    private val storedData = StoredData(application.applicationContext)

    var isPomodoroRunning = MutableLiveData<Boolean>()
    var currentTask = MutableLiveData<TaskEntity>()
    var currentTimeDurationInMillis = MutableLiveData<Long>()
    var statusText = MutableLiveData<String>()

    private var pomodoroTimer: CountDownTimer? = null
    private var workDurationInMillis: Long = 10000
    private var shortBreakDurationInMillis: Long = 2000
    private var longBreakDurationInMillis: Long = 5000
    private var pomodoroCountFinished = 0

    private var currentStatus = PomodoroStatus.WORK

    private var player: MediaPlayer? = null

    companion object {
        private const val NUMBER_OF_CYCLES = 4
    }

    init {
        repository = TaskRepositoryImpl(taskDao)
        currentTask.value = taskEntity
        pomodoroCountFinished = storedData.pomodoroCount
        statusText.value = ""
        setDurations()
        checkLongBrakePassed()
        mediaPlayerSetup()
    }

    fun startOrPauseTask() {
        isPomodoroRunning.value = if (isPomodoroRunning.value == true) {
            pausePomodoro()
            false
        } else {
            startPomodoro()
            true
        }
    }

    fun stopTask() {
        if (isPomodoroRunning.value != true) {
            saveNewPomodoroData(0)
            Toast.makeText(app.applicationContext, "Four pomodore cycle was interrupted.", Toast.LENGTH_SHORT).show()
        }
        resetPomodoro()
        currentStatus = PomodoroStatus.WORK
    }

    fun formatPomodoroTime(millis: Long): String {
        val minutes = ((millis / 60000) % 60).toInt()
        val seconds = ((millis / 1000) % 60).toInt()
        return "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
    }

    fun exit() {
        player?.stop()
        pomodoroTimer?.cancel()
        pomodoroTimer = null
    }

    private fun startPomodoro() {
        pomodoroTimer = object : CountDownTimer(currentTimeDurationInMillis.value!! - 100, 1000) {
            override fun onTick(millisUntilFinish: Long) {
                isPomodoroRunning.value = true
                currentTimeDurationInMillis.value = millisUntilFinish
                setStatusString()
            }

            override fun onFinish() {
                resetPomodoro()
                player?.start()
                if (currentStatus == PomodoroStatus.WORK) {
                    updateTask()
                    saveNewPomodoroData(pomodoroCountFinished + 1)
                    setShortOrLongBreak()
                    autoStartBreakDuration()
                } else {
                    currentStatus = PomodoroStatus.WORK
                    autoStartWorkDuration()
                }
            }
        }.start()
    }

    private fun pausePomodoro() {
        pomodoroTimer?.cancel()
    }

    private fun resetPomodoro() {
        if (pomodoroTimer != null) {
            pomodoroTimer?.cancel()
            pomodoroTimer = null
        }
        isPomodoroRunning.value = false
        currentTimeDurationInMillis.value = workDurationInMillis
        statusText.value = ""
    }

    private fun updateTask() {
        val id = taskEntity.id
        val title = taskEntity.title
        val description = taskEntity.description
        val numberOfPomodoro = taskEntity.numberOfPomodoros
        val numberOfFinishedPomodoro = taskEntity.numberOfFinishedPomodoros + 1

        taskEntity =
            TaskEntity(id, title, description, numberOfPomodoro, numberOfFinishedPomodoro)
        currentTask.value = taskEntity

        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTask(taskEntity)
        }
    }

    private fun autoStartBreakDuration() {
        if (settingsPomodoro.breakContinues) {
            android.os.Handler(Looper.getMainLooper()).postDelayed({
                startPomodoro()
            }, 1000)
        }
    }

    private fun autoStartWorkDuration() {
        if (settingsPomodoro.workContinues) {
            android.os.Handler(Looper.getMainLooper()).postDelayed({
                startPomodoro()
            }, 1000)
        }
    }

    private fun setShortOrLongBreak() {
        if (pomodoroCountFinished == NUMBER_OF_CYCLES) {
            currentTimeDurationInMillis.value = longBreakDurationInMillis
            currentStatus = PomodoroStatus.LONG_BREAK
            saveNewPomodoroData(0)
        } else {
            currentTimeDurationInMillis.value = shortBreakDurationInMillis
            currentStatus = PomodoroStatus.SHORT_BREAK
        }
    }

    private fun setStatusString() {
        when (currentStatus) {
            PomodoroStatus.WORK -> {
                statusText.value = "${app.resources.getString(R.string.statusWork)} (${pomodoroCountFinished + 1}/$NUMBER_OF_CYCLES)"
            }
            PomodoroStatus.SHORT_BREAK -> {
                statusText.value = "${app.resources.getString(R.string.statusShortBreak)} ($pomodoroCountFinished/$NUMBER_OF_CYCLES)"
            }
            PomodoroStatus.LONG_BREAK -> {
                statusText.value = app.resources.getString(R.string.statusLongBreak)
            }
        }
    }

    /**
     * We need to remember the time of the last Pomodoro and the number of Pomodoros already passed.
     */
    private fun saveNewPomodoroData(pomodoroCount: Int) {
        pomodoroCountFinished = pomodoroCount
        storedData.pomodoroCount = pomodoroCount
        storedData.dateTime = LocalDateTime.now().toString()
        storedData.save()
    }

    private fun setDurations() {
        workDurationInMillis = 1000 * 60 * settingsPomodoro.workDuration.toLong() //(1s*60=1min*25min)
        shortBreakDurationInMillis = 1000 * 60 * settingsPomodoro.shortBreakDuration.toLong()
        longBreakDurationInMillis = 1000 * 60 * settingsPomodoro.longBreakDuration.toLong()
        currentTimeDurationInMillis.value = workDurationInMillis
    }

    private fun checkLongBrakePassed() {
        val oldDateTime = LocalDateTime.parse(storedData.dateTime)
        val currentDateTime = LocalDateTime.now()
        if ((currentDateTime.minute - oldDateTime.minute) >= settingsPomodoro.longBreakDuration) {
            pomodoroCountFinished = 0
            storedData.pomodoroCount = 0
            storedData.dateTime = currentDateTime.toString()
            storedData.save()
        }
    }

    private fun mediaPlayerSetup() {
        try {
            val soundUri = Uri.parse("android.resource://eu.davidknotek.pomotodo/${R.raw.press_start}")
            player = MediaPlayer.create(app, soundUri)
            player?.isLooping = false

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}