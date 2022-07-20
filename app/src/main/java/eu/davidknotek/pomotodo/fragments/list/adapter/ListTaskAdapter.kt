package eu.davidknotek.pomotodo.fragments.list.adapter

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import eu.davidknotek.pomotodo.data.models.TaskEntity
import eu.davidknotek.pomotodo.databinding.RowTaskBinding
import eu.davidknotek.pomotodo.fragments.list.ListTaskFragmentDirections

class ListTaskAdapter: RecyclerView.Adapter<ListTaskAdapter.MyViewHolder>() {
    private var data = emptyList<TaskEntity>()

    class MyViewHolder(val itemHolding: RowTaskBinding): RecyclerView.ViewHolder(itemHolding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(RowTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = data[position]
        holder.itemHolding.tvTitle.text = currentItem.title
        holder.itemHolding.tvPomodoro.text = setPomodoroString(currentItem.pomoCount, currentItem.pomoFinish)

        holder.itemHolding.rowBackground.setOnClickListener { editTask(currentItem, holder) }
        holder.itemHolding.ivStartPomodoro.setOnClickListener { startPomodoro() }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(tasks: List<TaskEntity>) {
        data = tasks
        notifyDataSetChanged()
    }

    private fun editTask(taskEntity: TaskEntity, holder: MyViewHolder) {
        val action = ListTaskFragmentDirections.actionTaskListFragmentToEditTaskFragment(taskEntity)
        holder.itemView.findNavController().navigate(action)
    }

    private fun startPomodoro() {
        Log.d(TAG, "startPomodoro: Start Pomodoro")
    }

    private fun setPomodoroString(pomoCount: Int, pomoFinish: Int): String {
        var pomoString = ""

        for (i in 1..pomoFinish) {
            pomoString += "✅"
        }

        for (i in 1..pomoCount - pomoFinish) {
            pomoString += "\uD83D\uDFE9"
        }

        return pomoString
    }
}