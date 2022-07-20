package eu.davidknotek.pomotodo.data.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "tasks")
@Parcelize
data class TaskEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var title: String,
    var description: String,

    @ColumnInfo(name = "pomo_count")
    var pomoCount: Int,

    @ColumnInfo(name = "pomo_finish")
    var pomoFinish: Int
): Parcelable
