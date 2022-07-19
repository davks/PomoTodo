package eu.davidknotek.pomotodo.data.repository

import androidx.lifecycle.LiveData
import eu.davidknotek.pomotodo.data.models.TaskEntity

interface TaskRepository {
    val getAllTasks: LiveData<List<TaskEntity>>
    suspend fun addNewTask(taskEntity: TaskEntity)
    suspend fun updateTask(taskEntity: TaskEntity)
    suspend fun deleteTask(taskEntity: TaskEntity)
    suspend fun deleteAllTasks()

}