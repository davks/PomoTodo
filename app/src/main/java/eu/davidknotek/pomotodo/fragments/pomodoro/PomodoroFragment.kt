package eu.davidknotek.pomotodo.fragments.pomodoro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.davidknotek.pomotodo.databinding.FragmentPomodoroBinding

class PomodoroFragment : Fragment() {
    private lateinit var binding: FragmentPomodoroBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPomodoroBinding.inflate(layoutInflater)
        return binding.root
    }
}