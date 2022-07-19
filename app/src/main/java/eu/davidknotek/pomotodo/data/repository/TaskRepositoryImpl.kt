package eu.davidknotek.pomotodo.data.repository

import androidx.lifecycle.LiveData
import eu.davidknotek.pomotodo.data.TaskDao
import eu.davidknotek.pomotodo.data.models.TaskEntity

class TaskRepositoryImpl(private val taskDao: TaskDao): TaskRepository {
    override val getAllTasks: LiveData<List<TaskEntity>> = taskDao.getAllTasks()

    override suspend fun addNewTask(taskEntity: TaskEntity) {
        taskDao.addTask(taskEntity)
    }

    override suspend fun updateTask(taskEntity: TaskEntity) {
        taskDao.updateTask(taskEntity)
    }

    override suspend fun deleteTask(taskEntity: TaskEntity) {
        taskDao.deleteTask(taskEntity)
    }

    override suspend fun deleteAllTasks() {
        taskDao.deleteAllTask()
    }
}