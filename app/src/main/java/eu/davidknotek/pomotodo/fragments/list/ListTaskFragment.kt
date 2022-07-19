package eu.davidknotek.pomotodo.fragments.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import eu.davidknotek.pomotodo.R
import eu.davidknotek.pomotodo.databinding.FragmentListTaskBinding

class ListTaskFragment : Fragment() {
    private lateinit var binding: FragmentListTaskBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListTaskBinding.inflate(layoutInflater)
        binding.faAddTask.setOnClickListener {
            findNavController().navigate(R.id.action_todoListFragment_to_addTodoFragment)
        }

        return binding.root
    }
}