package eu.davidknotek.pomotodo.fragments.pomodoro

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import eu.davidknotek.pomotodo.R
import eu.davidknotek.pomotodo.data.viewmodels.PomodoroViewModel
import eu.davidknotek.pomotodo.data.viewmodels.PomodoroViewModelFactory
import eu.davidknotek.pomotodo.databinding.FragmentPomodoroBinding
import eu.davidknotek.pomotodo.util.setPomodoroString


class PomodoroFragment : Fragment() {
    private lateinit var binding: FragmentPomodoroBinding
    private val args by navArgs<PomodoroFragmentArgs>()
    private val pomodoroViewModel: PomodoroViewModel by viewModels {
        PomodoroViewModelFactory(requireActivity().application, args.currentItem)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPomodoroBinding.inflate(layoutInflater)

        setupPomodoroFragment()
        setupListeners()
        setupObservers()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val myWindow = (activity as AppCompatActivity).window
        (activity as AppCompatActivity).supportActionBar?.hide()
        myWindow.statusBarColor = Color.BLACK
        myWindow.navigationBarColor = Color.BLACK
    }

    override fun onStop() {
        super.onStop()
        // Show a toolbar from activity
        (activity as AppCompatActivity).supportActionBar?.show()
    }

    private fun setupPomodoroFragment() {
        binding.tvTask.text = args.currentItem.title
        binding.tvTaskPomodoros.text = setPomodoroString(args.currentItem.numberOfPomodoros, args.currentItem.numberOfFinishedPomodoros)
    }

    private fun setupListeners() {
        binding.ivStartPausePomodoro.setOnClickListener {
            pomodoroViewModel.startOrPauseTask()
        }

        binding.ivStopPomodoro.setOnClickListener {
            pomodoroViewModel.stopTask()
        }
    }

    private fun setupObservers() {
        pomodoroViewModel.restSeconds.observe(viewLifecycleOwner) {
            binding.tvTime.text = setPomodoroTimeFormat(it)
        }

        pomodoroViewModel.isPomodoroRunning.observe(viewLifecycleOwner) {
            if (!it) {
                binding.ivStartPausePomodoro.setImageResource(R.drawable.ic_play_arrow)
            } else {
                binding.ivStartPausePomodoro.setImageResource(R.drawable.ic_pause)
            }
        }

        pomodoroViewModel.task.observe(viewLifecycleOwner) {
            binding.tvTaskPomodoros.text = setPomodoroString(args.currentItem.numberOfPomodoros, it.numberOfFinishedPomodoros)
        }
    }

    private fun setPomodoroTimeFormat(time: Int): String {
        val minutes = getDoubleDigits(time / 60)
        val seconds = getDoubleDigits(time % 60)

        return "$minutes:$seconds"
    }

    private fun getDoubleDigits(number: Int): String {
        return when {
            number.toString().length == 1 -> "0$number"
            else -> number.toString()
        }
    }
}