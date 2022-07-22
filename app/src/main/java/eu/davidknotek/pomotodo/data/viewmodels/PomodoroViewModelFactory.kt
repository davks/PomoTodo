package eu.davidknotek.pomotodo.data.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eu.davidknotek.pomotodo.data.models.TaskEntity

class PomodoroViewModelFactory(
    private val application: Application,
    private val taskEntity: TaskEntity
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        PomodoroViewModel(application, taskEntity) as T
}