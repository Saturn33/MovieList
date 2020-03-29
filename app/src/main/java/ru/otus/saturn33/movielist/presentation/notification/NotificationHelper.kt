package ru.otus.saturn33.movielist.presentation.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ru.otus.saturn33.movielist.R
import ru.otus.saturn33.movielist.data.entity.MovieDTO
import ru.otus.saturn33.movielist.presentation.view.activity.MainActivity

object NotificationHelper {
    const val POSTPONE_REQUEST_ACTION = "OPEN_MOVIE"
    const val POSTPONE_EXTRA_MOVIE = "movie"
    private const val POSTPONE_REQUEST_CODE = 10
    private const val SIMPLE_NOTIFY_ID = 0
    private const val POSTPONE_NOTIFY_ID = 1
    private const val POSTPONE_CHANNEL_ID = "postpone_channel_id"
    private var channelCreated = false

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.notification_channel_name)
            val descriptionText = context.getString(R.string.notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(POSTPONE_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    private fun createNotificationChannelIfNotCreated(context: Context) {
        if (!channelCreated)
            createNotificationChannel(context)
        channelCreated = true
    }

/*    fun simpleNotification(context: Context, title: String, text: String) {
        createNotificationChannelIfNotCreated(context)
        val builder = NotificationCompat.Builder(context, POSTPONE_CHANNEL_ID)
            .setSmallIcon(R.drawable.postpone_yes)
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.priority = NotificationManager.IMPORTANCE_HIGH
        }
        NotificationManagerCompat.from(context).notify(SIMPLE_NOTIFY_ID, builder.build())
    }
*/

    fun postponeNotification(context: Context, movie: MovieDTO) {
        createNotificationChannelIfNotCreated(context)
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(POSTPONE_EXTRA_MOVIE, movie)
            action = POSTPONE_REQUEST_ACTION
        }
        val pendIntent = PendingIntent.getActivity(context, POSTPONE_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(context, POSTPONE_CHANNEL_ID)
            .setSmallIcon(R.drawable.postpone_yes)
            .setContentTitle(context.getString(R.string.title_reminder))
            .setContentText(context.getString(R.string.time_to_watch) + " '${movie.name}'")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendIntent)
            .setAutoCancel(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.priority = NotificationManager.IMPORTANCE_HIGH
        }
        NotificationManagerCompat.from(context).notify(POSTPONE_NOTIFY_ID, builder.build())
    }

}
