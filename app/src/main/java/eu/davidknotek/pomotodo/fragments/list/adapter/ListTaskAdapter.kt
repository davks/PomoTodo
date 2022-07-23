package eu.davidknotek.pomotodo.fragments.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import eu.davidknotek.pomotodo.data.models.TaskEntity
import eu.davidknotek.pomotodo.databinding.RowTaskBinding
import eu.davidknotek.pomotodo.fragments.list.ListTaskFragmentDirections
import eu.davidknotek.pomotodo.util.formatFinishedPomodoro

class ListTaskAdapter: RecyclerView.Adapter<ListTaskAdapter.MyViewHolder>() {
    private var tasks = emptyList<TaskEntity>()

    class MyViewHolder(val itemHolding: RowTaskBinding): RecyclerView.ViewHolder(itemHolding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(RowTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = tasks[position]
        holder.itemHolding.tvTitle.text = currentItem.title
        holder.itemHolding.tvPomodoro.text = formatFinishedPomodoro(currentItem.numberOfPomodoros, currentItem.numberOfFinishedPomodoros)

        holder.itemHolding.rowBackground.setOnClickListener { editTask(currentItem, holder) }
        holder.itemHolding.ivStartPomodoro.setOnClickListener { startPomodoro(currentItem, holder) }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    fun setData(newTasks: List<TaskEntity>) {
        val taskDiffUtil = TaskDiffUtil(tasks, newTasks)
        val taskDiffUtilResult = DiffUtil.calculateDiff(taskDiffUtil)
        tasks = newTasks
        taskDiffUtilResult.dispatchUpdatesTo(this)
    }

    private fun editTask(taskEntity: TaskEntity, holder: MyViewHolder) {
        val action = ListTaskFragmentDirections.actionListTaskFragmentToEditTaskFragment(taskEntity)
        holder.itemView.findNavController().navigate(action)
    }

    private fun startPomodoro(taskEntity: TaskEntity, holder: MyViewHolder) {
        val action = ListTaskFragmentDirections.actionListTaskFragmentToPomodoroFragment(taskEntity)
        holder.itemView.findNavController().navigate(action)
    }
}