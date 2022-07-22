package eu.davidknotek.pomotodo.data.viewmodels

import android.app.Application
import android.content.ContentValues.TAG
import android.os.CountDownTimer
import android.os.Looper
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import eu.davidknotek.pomotodo.R
import eu.davidknotek.pomotodo.data.TaskDatabase
import eu.davidknotek.pomotodo.data.models.TaskEntity
import eu.davidknotek.pomotodo.data.repository.TaskRepository
import eu.davidknotek.pomotodo.data.repository.TaskRepositoryImpl
import eu.davidknotek.pomotodo.util.SettingsPomodoro
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PomodoroViewModel(application: Application, var taskEntity: TaskEntity) :
    AndroidViewModel(application) {
    private val taskDao = TaskDatabase.getDatabase(application).taskDao()
    private val repository: TaskRepository
    private val settingsPomodoro = SettingsPomodoro(application.applicationContext)

    var isPomodoroRunning = MutableLiveData<Boolean>()
    var task = MutableLiveData<TaskEntity>()
    var currentTimeDurationInMillis = MutableLiveData<Long>()
    var status = MutableLiveData<String>()

    private var pomodoroTimer: CountDownTimer? = null
    private var workDurationInMillis: Long = 10000
    private var breakDurationInMillis: Long = 5000

    private var currentStatus = PomodoroStatus.WORK
    private var pomodoroCount = 0

    init {
        repository = TaskRepositoryImpl(taskDao)
        this.task.value = taskEntity
        settingsPomodoro.load()
        workDurationInMillis = 1000 * 60 * settingsPomodoro.workDuration.toLong() //(1s*60=1min*25min)
        breakDurationInMillis = 1000 * 60 * settingsPomodoro.breakDuration.toLong()
        currentTimeDurationInMillis.value = workDurationInMillis
        status.value = ""
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
        resetPomodoro()
        currentStatus = PomodoroStatus.WORK
    }

    fun formatPomodoroTime(millis: Long): String {
        val minutes = ((millis / 60000) % 60).toInt()
        val seconds = ((millis / 1000) % 60).toInt()
        return "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
    }

    private fun startPomodoro() {
        pomodoroTimer = object : CountDownTimer(currentTimeDurationInMillis.value!!, 1000) {
            override fun onTick(millisUntilFinish: Long) {
                isPomodoroRunning.value = true
                currentTimeDurationInMillis.value = millisUntilFinish
                setStatusString()
            }

            override fun onFinish() {
                resetPomodoro()
                if (currentStatus == PomodoroStatus.WORK) {
                    updateTask()
                    startBreakDuration()
                    currentStatus = PomodoroStatus.BREAK
                    pomodoroCount++
                    Log.d(TAG, "onFinish: $pomodoroCount")
                } else {
                    currentStatus = PomodoroStatus.WORK
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
        status.value = ""
    }

    private fun updateTask() {
        val id = taskEntity.id
        val title = taskEntity.title
        val description = taskEntity.description
        val numberOfPomodoros = taskEntity.numberOfPomodoros
        val numberOfFinishedPomodoros = taskEntity.numberOfFinishedPomodoros + 1

        taskEntity =
            TaskEntity(id, title, description, numberOfPomodoros, numberOfFinishedPomodoros)
        task.value = taskEntity

        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTask(taskEntity)
        }
    }

    private fun startBreakDuration() {
        currentTimeDurationInMillis.value = breakDurationInMillis
        if (settingsPomodoro.breakContinues) {
            android.os.Handler(Looper.getMainLooper()).postDelayed({
                startPomodoro()
            }, 2000)
        }
    }

    private fun setStatusString() {
        if (currentStatus == PomodoroStatus.WORK) {
            status.value = getApplication<Application>().resources.getString(R.string.statusWork)
        } else {
            status.value = getApplication<Application>().resources.getString(R.string.statusBreak)
        }
    }
}