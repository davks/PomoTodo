package eu.davidknotek.pomotodo.data

import androidx.lifecycle.LiveData
import androidx.room.*
import eu.davidknotek.pomotodo.data.models.TaskEntity

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY id ASC")
    fun getAllData(): LiveData<List<TaskEntity>>

    @Insert
    fun addTask(taskEntity: TaskEntity)

    @Update
    fun updateTask(taskEntity: TaskEntity)

    @Delete
    fun deleteTask(taskEntity: TaskEntity)

    @Query("DELETE FROM tasks")
    fun deleteAllTask()
}