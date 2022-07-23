package eu.davidknotek.pomotodo.data.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import eu.davidknotek.pomotodo.data.models.TaskEntity

class SharedViewModel(application: Application): AndroidViewModel(application) {
    val isEmptyDatabase = MutableLiveData(false)

    fun checkIfDatabaseEmpty(tasks: List<TaskEntity>) {
        isEmptyDatabase.value = tasks.isEmpty()
    }
}