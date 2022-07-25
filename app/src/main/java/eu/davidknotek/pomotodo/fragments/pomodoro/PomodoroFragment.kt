package eu.davidknotek.pomotodo.fragments.pomodoro

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import eu.davidknotek.pomotodo.R
import eu.davidknotek.pomotodo.data.viewmodels.PomodoroViewModel
import eu.davidknotek.pomotodo.data.viewmodels.PomodoroViewModelFactory
import eu.davidknotek.pomotodo.databinding.FragmentPomodoroBinding
import eu.davidknotek.pomotodo.util.formatFinishedPomodoro


class PomodoroFragment : Fragment() {
    private lateinit var binding: FragmentPomodoroBinding
    private val args by navArgs<PomodoroFragmentArgs>()
    private val pomodoroViewModel: PomodoroViewModel by viewModels {
        PomodoroViewModelFactory(requireActivity().application, args.currentItem)
    }

    // Notification
    private val NOTIFY_ID = 1000
    private lateinit var notificationManager: NotificationManager
    private lateinit var notification: Notification

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPomodoroBinding.inflate(layoutInflater)

        setupPomodoroFragment()
        setupListeners()
        setupObservers()
        showNotification()

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

    override fun onDestroy() {
        super.onDestroy()
        pomodoroViewModel.exit()
        notificationManager.cancel(NOTIFY_ID)
//        NotificationManagerCompat.from(requireContext()).cancelAll()
    }

    private fun showNotification(text: String = "Text"): Notification {
        val channelID = "10000"
        notificationManager = requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(requireContext(), channelID)
            .setSmallIcon(R.drawable.ic_av_timer)
            .setContentTitle(args.currentItem.title)
            .setContentText(text)
            .setOnlyAlertOnce(true)
            .setOngoing(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelID, "Pomodoro timer", NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            notificationManager.createNotificationChannel(channel)
            builder.setChannelId(channelID)
        }

        return builder.build()
    }

    private fun setupPomodoroFragment() {
        binding.tvTask.text = args.currentItem.title
        binding.tvTaskPomodoros.text = formatFinishedPomodoro(args.currentItem.numberOfPomodoros, args.currentItem.numberOfFinishedPomodoros)
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
        pomodoroViewModel.currentTimeDurationInMillis.observe(viewLifecycleOwner) {
            binding.tvTime.text = pomodoroViewModel.formatPomodoroTime(it)

            notification = showNotification(pomodoroViewModel.formatPomodoroTime(it))
            notificationManager.notify(NOTIFY_ID, notification)
        }

        pomodoroViewModel.isPomodoroRunning.observe(viewLifecycleOwner) {
            if (it) {
                binding.ivStartPausePomodoro.setImageResource(R.drawable.ic_pause)
            } else {
                binding.ivStartPausePomodoro.setImageResource(R.drawable.ic_play_arrow)
            }
        }

        pomodoroViewModel.currentTask.observe(viewLifecycleOwner) {
            binding.tvTaskPomodoros.text = formatFinishedPomodoro(args.currentItem.numberOfPomodoros, it.numberOfFinishedPomodoros)
        }

        pomodoroViewModel.statusText.observe(viewLifecycleOwner) {
            binding.tvStatus.text = it
        }
    }
}