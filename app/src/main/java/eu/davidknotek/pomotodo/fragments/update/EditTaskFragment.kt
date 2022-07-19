package eu.davidknotek.pomotodo.fragments.update

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.davidknotek.pomotodo.databinding.FragmentEditTaskBinding

class EditTaskFragment : Fragment() {
    private lateinit var binding: FragmentEditTaskBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditTaskBinding.inflate(layoutInflater)

        return binding.root
    }
}