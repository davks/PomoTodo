package eu.davidknotek.pomotodo.data.viewmodels

import android.app.Application
import android.os.CountDownTimer
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import eu.davidknotek.pomotodo.data.TaskDatabase
import eu.davidknotek.pomotodo.data.models.TaskEntity
import eu.davidknotek.pomotodo.data.repository.TaskRepository
import eu.davidknotek.pomotodo.data.repository.TaskRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PomodoroViewModel(application: Application): AndroidViewModel(application) {
    private val taskDao = TaskDatabase.getDatabase(application).taskDao()
    private val repository: TaskRepository

    var isPomodoroRunning = MutableLiveData<Boolean>()
        private set
    var restSeconds = MutableLiveData<Int>()
    var numberOfFinishedPomodoros = MutableLiveData<Int>()

    private var pomodoroTimer: CountDownTimer? = null
    private var workDuration: Long = 1000 * 10 //(1s*60=1min*25min)
    private var pauseDuration: Long = 1000 * 5
    private var restTimeDuration = workDuration

    private var isPauseDurationFinished = false
    lateinit var taskEntity: TaskEntity

    init {
        repository = TaskRepositoryImpl(taskDao)
        restSeconds.value = (restTimeDuration / 1000).toInt()
    }

    fun updateTask() {
        val id = taskEntity.id
        val title = taskEntity.title
        val description = taskEntity.description
        val numberOfPomodoros = taskEntity.numberOfPomodoros
        val numberOfFinishedPomodoros = taskEntity.numberOfFinishedPomodoros + 1
        this.numberOfFinishedPomodoros.value = numberOfFinishedPomodoros

        taskEntity = TaskEntity(id, title, description, numberOfPomodoros, numberOfFinishedPomodoros)

        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTask(taskEntity)
        }
    }

    fun startOrPauseTask(taskEntity: TaskEntity) {
        this.taskEntity = taskEntity
        numberOfFinishedPomodoros.value = taskEntity.numberOfFinishedPomodoros

        isPomodoroRunning.value = if (isPomodoroRunning.value == true) {
            pausePomodoro()
            false
        } else {
            startPomodoro()
            true
        }
    }

    fun setCurrentTask(taskEntity: TaskEntity) {
        this.taskEntity = taskEntity
    }

    fun stopTask() {
        resetPomodoro()
        isPauseDurationFinished = false
    }

    private fun startPomodoro() {
        pomodoroTimer = object : CountDownTimer(restTimeDuration, 1000) {
            override fun onTick(millisUntilFinish: Long) {
                isPomodoroRunning.value = true
                restTimeDuration = millisUntilFinish
                restSeconds.value = (restTimeDuration / 1000).toInt()
            }

            override fun onFinish() {
                resetPomodoro()
                if (!isPauseDurationFinished) {
                    updateTask()
                    startPauseDuration()
                    isPauseDurationFinished = true
                } else {
                    isPauseDurationFinished = false
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
        restTimeDuration = workDuration
        restSeconds.value = (restTimeDuration / 1000).toInt()
    }

    private fun startPauseDuration() {
        restTimeDuration = pauseDuration
        restSeconds.value = (restTimeDuration / 1000).toInt()
        android.os.Handler(Looper.getMainLooper()).postDelayed({
            startPomodoro()
        }, 2000 // value in milliseconds
        )
    }
}