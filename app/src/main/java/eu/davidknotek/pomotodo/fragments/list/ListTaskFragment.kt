package eu.davidknotek.pomotodo.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import eu.davidknotek.pomotodo.R
import eu.davidknotek.pomotodo.data.viewmodels.TaskViewModel
import eu.davidknotek.pomotodo.databinding.FragmentListTaskBinding
import eu.davidknotek.pomotodo.fragments.list.adapter.ListTaskAdapter

class ListTaskFragment : Fragment() {
    private lateinit var binding: FragmentListTaskBinding
    private val adapter: ListTaskAdapter by lazy { ListTaskAdapter() }
    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListTaskBinding.inflate(layoutInflater)

        binding.faAddTask.setOnClickListener {
            findNavController().navigate(R.id.action_taskListFragment_to_addTaskFragment)
        }

        setHasOptionsMenu(true)
        setupRecyclerView()

        taskViewModel.getAllTasks.observe(viewLifecycleOwner) {
            adapter.setData(it)
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.rvTasks.adapter = adapter
        binding.rvTasks.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.deleteAllTasks -> {
                deleteAllTasks()
                true
            }
            R.id.settings -> {
                findNavController().navigate(R.id.action_listTaskFragment_to_settingsFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteAllTasks() {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete All tasks")
            .setMessage("Are you sure you want to delete ALL tasks?")
            .setPositiveButton("yes") {_,_->
                taskViewModel.deleteAllTasks()
            }
            .setNegativeButton("no") {_,_->}
            .create().show()
    }
}