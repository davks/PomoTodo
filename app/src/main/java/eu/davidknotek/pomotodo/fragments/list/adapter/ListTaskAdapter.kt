package eu.davidknotek.pomotodo.fragments.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import eu.davidknotek.pomotodo.data.models.TaskEntity
import eu.davidknotek.pomotodo.databinding.RowTaskBinding
import eu.davidknotek.pomotodo.fragments.list.ListTaskFragmentDirections
import eu.davidknotek.pomotodo.util.formatPomodoroFinished

class ListTaskAdapter: RecyclerView.Adapter<ListTaskAdapter.MyViewHolder>() {
    private var data = emptyList<TaskEntity>()

    class MyViewHolder(val itemHolding: RowTaskBinding): RecyclerView.ViewHolder(itemHolding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(RowTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = data[position]
        holder.itemHolding.tvTitle.text = currentItem.title
        holder.itemHolding.tvPomodoro.text = formatPomodoroFinished(currentItem.numberOfPomodoros, currentItem.numberOfFinishedPomodoros)

        holder.itemHolding.rowBackground.setOnClickListener { editTask(currentItem, holder) }
        holder.itemHolding.ivStartPomodoro.setOnClickListener { startPomodoro(currentItem, holder) }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(tasks: List<TaskEntity>) {
        val taskDiffUtil = TaskDiffUtil(data, tasks)
        val taskDiffUtilResult = DiffUtil.calculateDiff(taskDiffUtil)
        data = tasks
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