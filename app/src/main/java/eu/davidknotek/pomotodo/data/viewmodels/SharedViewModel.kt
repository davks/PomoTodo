package eu.davidknotek.pomotodo.data.viewmodels

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import eu.davidknotek.pomotodo.R
import eu.davidknotek.pomotodo.data.models.TaskEntity

class SharedViewModel(application: Application): AndroidViewModel(application) {
    private val app = application
    val isEmptyDatabase = MutableLiveData(false)

    // Notification
    private val notifyId = 1000
    private lateinit var notificationManager: NotificationManager
    private lateinit var notification: Notification

    fun checkIfDatabaseEmpty(tasks: List<TaskEntity>) {
        isEmptyDatabase.value = tasks.isEmpty()
    }

    fun refreshNotification(content: String, title: String) {
        notification = showNotification(content, title)
        notificationManager.notify(notifyId, notification)
    }

    fun cancelNotification() {
        notificationManager.cancel(notifyId)
    }

    private fun showNotification(content: String, title: String): Notification {
        val channelID = "10000"
        notificationManager = app.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(app.applicationContext, channelID)
            .setSmallIcon(R.drawable.ic_av_timer)
            .setContentTitle(title)
            .setContentText(content)
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
}