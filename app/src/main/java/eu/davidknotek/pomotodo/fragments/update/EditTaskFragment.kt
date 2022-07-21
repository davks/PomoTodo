package eu.davidknotek.pomotodo.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import eu.davidknotek.pomotodo.R
import eu.davidknotek.pomotodo.data.models.TaskEntity
import eu.davidknotek.pomotodo.data.viewmodels.TaskViewModel
import eu.davidknotek.pomotodo.databinding.FragmentEditTaskBinding

class EditTaskFragment : Fragment() {
    private lateinit var binding: FragmentEditTaskBinding
    private val taskViewModel: TaskViewModel by viewModels()
    private val args by navArgs<EditTaskFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditTaskBinding.inflate(layoutInflater)

        binding.sbPomodoro.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, currentProgress: Int, userInitiated: Boolean) {
                binding.tvPomodoro.text = currentProgress.toString()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })

        setHasOptionsMenu(true)
        setEditTask()

        return binding.root
    }

    private fun setEditTask() {
        binding.etTitle.setText(args.currentItem.title)
        binding.etDescription.setText(args.currentItem.description)
        binding.sbPomodoro.progress = args.currentItem.numberOfPomodoros
        binding.tvPomodoro.text = args.currentItem.numberOfPomodoros.toString()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.saveTask -> {
                saveTask()
                true
            }
            R.id.deleteTask -> {
                deleteTask()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveTask() {
        val title = binding.etTitle.text.toString()
        val numberOfPomodoros = binding.sbPomodoro.progress
        val description = binding.etDescription.text.toString()

        if (title.isNotEmpty()) {
            val task = TaskEntity(args.currentItem.id, title, description, numberOfPomodoros.toInt(), args.currentItem.numberOfFinishedPomodoros)
            taskViewModel.updateTask(task)
            findNavController().navigate(R.id.action_editTaskFragment_to_taskListFragment)
        } else {
            Toast.makeText(context, "Please, fill out the title!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteTask() {
        AlertDialog.Builder(requireContext())
            .setPositiveButton("Yes") {_, _ ->
                taskViewModel.deleteTask(args.currentItem)
                findNavController().navigate(R.id.action_editTaskFragment_to_taskListFragment)
            }
            .setNegativeButton("No") {_, _ ->}
            .setTitle("Delete task")
            .setMessage("Are you sure you want to delete the task: ${args.currentItem.title}?")
            .create().show()
    }
}