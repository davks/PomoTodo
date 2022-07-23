package eu.davidknotek.pomotodo.data

import androidx.lifecycle.LiveData
import androidx.room.*
import eu.davidknotek.pomotodo.data.models.TaskEntity

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks ORDER BY id ASC")
    fun getAllTasks(): LiveData<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(taskEntity: TaskEntity)

    @Update
    suspend fun updateTask(taskEntity: TaskEntity)

    @Delete
    suspend fun deleteTask(taskEntity: TaskEntity)

    @Query("DELETE FROM tasks")
    suspend fun deleteAllTasks()
}