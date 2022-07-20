package eu.davidknotek.pomotodo.data.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import eu.davidknotek.pomotodo.data.TaskDatabase
import eu.davidknotek.pomotodo.data.models.TaskEntity
import eu.davidknotek.pomotodo.data.repository.TaskRepository
import eu.davidknotek.pomotodo.data.repository.TaskRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(application: Application): AndroidViewModel(application) {
    private val taskDao = TaskDatabase.getDatabase(application).taskDao()
    private val repository: TaskRepository

    val getAllTasks: LiveData<List<TaskEntity>>

    init {
        repository = TaskRepositoryImpl(taskDao)
        getAllTasks = repository.getAllTasks
    }

    fun addTask(taskEntity: TaskEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addNewTask(taskEntity)
        }
    }

    fun updateTask(taskEntity: TaskEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTask(taskEntity)
        }
    }

    fun deleteTask(taskEntity: TaskEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTask(taskEntity)
        }
    }

    fun deleteAllTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllTasks()
        }
    }
}