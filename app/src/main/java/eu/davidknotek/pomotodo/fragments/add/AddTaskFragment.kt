package eu.davidknotek.pomotodo.fragments.add

import android.os.Bundle
import android.view.*
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import eu.davidknotek.pomotodo.R
import eu.davidknotek.pomotodo.data.models.TaskEntity
import eu.davidknotek.pomotodo.data.viewmodels.TaskViewModel
import eu.davidknotek.pomotodo.databinding.FragmentAddTaskBinding

class AddTaskFragment : Fragment() {
    private lateinit var binding: FragmentAddTaskBinding
    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTaskBinding.inflate(layoutInflater)

        setHasOptionsMenu(true)

        binding.sbPomodoro.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, currentProgress: Int, userInitiated: Boolean) {
                binding.tvPomodoro.text = currentProgress.toString()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.saveTask -> {
                saveTask()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveTask() {
        val title = binding.etTitle.text.toString()
        val pomodoroCount = binding.sbPomodoro.progress
        val description = binding.etDescription.text.toString()

        if (title.isNotEmpty()) {
            val task = TaskEntity(0, title, description, pomodoroCount, 0)
            taskViewModel.addTask(task)
            findNavController().navigate(R.id.action_addTaskFragment_to_taskListFragment)
        } else {
            Toast.makeText(context, "Please, fill out the title!", Toast.LENGTH_SHORT).show()
        }
    }
}