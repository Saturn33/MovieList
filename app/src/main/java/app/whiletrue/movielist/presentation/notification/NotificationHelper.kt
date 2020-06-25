package app.whiletrue.movielist.presentation.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import app.whiletrue.movielist.R
import app.whiletrue.movielist.data.entity.MovieDTO
import app.whiletrue.movielist.presentation.view.activity.MainActivity

object NotificationHelper {
    const val POSTPONE_REQUEST_ACTION = "POSTPONE_OPEN_MOVIE"
    const val DETAILED_REQUEST_ACTION = "DETAILED_OPEN_MOVIE"
    const val EXTRA_MOVIE = "movie"
    private const val DETAILED_REQUEST_CODE = 5
    private const val POSTPONE_REQUEST_CODE = 10
    private const val DETAILED_NOTIFY_ID = 0
    private const val POSTPONE_NOTIFY_ID = 1
    private const val DETAILED_CHANNEL_ID = "detailed_channel_id"
    private const val POSTPONE_CHANNEL_ID = "postpone_channel_id"
    private var detailedChannelCreated = false
    private var postponeChannelCreated = false

    private fun createPostponeNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.postpone_notification_channel_name)
            val descriptionText = context.getString(R.string.postpone_notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(POSTPONE_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    private fun createPostponeNotificationChannelIfNotCreated(context: Context) {
        if (!postponeChannelCreated)
            createPostponeNotificationChannel(context)
        postponeChannelCreated = true
    }

    private fun createDetailedNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.detailed_notification_channel_name)
            val descriptionText = context.getString(R.string.detailed_notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(DETAILED_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    private fun createDetailedNotificationChannelIfNotCreated(context: Context) {
        if (!detailedChannelCreated)
            createDetailedNotificationChannel(context)
        detailedChannelCreated = true
    }

    fun detailedMovieNotification(
        context: Context,
        movie: MovieDTO,
        title: String?,
        text: String?
    ) {
        createDetailedNotificationChannelIfNotCreated(context)
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(EXTRA_MOVIE, movie)
            action = DETAILED_REQUEST_ACTION
        }
        val pendIntent = PendingIntent.getActivity(
            context,
            DETAILED_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder = NotificationCompat.Builder(context, DETAILED_CHANNEL_ID)
            .setSmallIcon(R.drawable.movie_filler)
            .setContentTitle(title ?: context.getString(R.string.title_detailed))
            .setContentText(text ?: context.getString(R.string.text_detailed) + " '${movie.name}'")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendIntent)
            .setStyle(NotificationCompat.BigTextStyle())
            .setAutoCancel(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.priority = NotificationManager.IMPORTANCE_HIGH
        }
        NotificationManagerCompat.from(context).notify(DETAILED_NOTIFY_ID, builder.build())
    }

    fun postponeNotification(context: Context, movie: MovieDTO) {
        createPostponeNotificationChannelIfNotCreated(context)
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(EXTRA_MOVIE, movie)
            action = POSTPONE_REQUEST_ACTION
        }
        val pendIntent = PendingIntent.getActivity(
            context,
            POSTPONE_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
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
