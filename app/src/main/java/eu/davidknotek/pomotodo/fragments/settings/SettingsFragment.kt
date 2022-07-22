package eu.davidknotek.pomotodo.fragments.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import eu.davidknotek.pomotodo.databinding.FragmentSettingsBinding
import eu.davidknotek.pomotodo.util.SettingsPomodoro

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var settingsPomodoro: SettingsPomodoro

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        settingsPomodoro = SettingsPomodoro(requireContext())
        settingsPomodoro.load()

        setSettingsFragment()
        setSettingsListeners()

        return binding.root
    }

    private fun setSettingsListeners() {
        binding.sbLengthOfPomodoro.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, currentProgress: Int, p2: Boolean) {
                binding.tvLengthOfPomo.text = currentProgress.toString()
                settingsPomodoro.workDuration = currentProgress
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {
                settingsPomodoro.save()
            }

        })

        binding.sbLengthOfBreak.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, currentProgress: Int, p2: Boolean) {
                binding.tvLengthOfBreak.text = currentProgress.toString()
                settingsPomodoro.breakDuration = currentProgress
            }

            override fun onStartTrackingTouch(p0: SeekBar?) { }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                settingsPomodoro.save()
            }

        })

        binding.swAutomaticStartBreak.setOnClickListener {
            settingsPomodoro.breakContinues = binding.swAutomaticStartBreak.isChecked
            settingsPomodoro.save()
        }
    }

    private fun setSettingsFragment() {
        binding.tvLengthOfPomo.text = settingsPomodoro.workDuration.toString()
        binding.tvLengthOfBreak.text = settingsPomodoro.breakDuration.toString()
        binding.swAutomaticStartBreak.isChecked = settingsPomodoro.breakContinues

        binding.sbLengthOfPomodoro.progress = settingsPomodoro.workDuration
        binding.sbLengthOfBreak.progress = settingsPomodoro.breakDuration
    }
}