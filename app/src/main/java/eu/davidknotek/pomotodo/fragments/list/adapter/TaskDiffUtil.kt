package eu.davidknotek.pomotodo.fragments.list.adapter

import androidx.recyclerview.widget.DiffUtil
import eu.davidknotek.pomotodo.data.models.TaskEntity

class TaskDiffUtil(
    private val oldList: List<TaskEntity>,
    private val newList: List<TaskEntity>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
                && oldList[oldItemPosition].title == newList[newItemPosition].title
                && oldList[oldItemPosition].description == newList[newItemPosition].description
                && oldList[oldItemPosition].numberOfPomodoros == newList[newItemPosition].numberOfPomodoros
                && oldList[oldItemPosition].numberOfFinishedPomodoros == newList[newItemPosition].numberOfFinishedPomodoros
    }
}