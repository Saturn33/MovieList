package ru.otus.saturn33.movielist.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ru.otus.saturn33.movielist.App
import ru.otus.saturn33.movielist.data.entity.MovieDTO
import ru.otus.saturn33.movielist.presentation.notification.NotificationHelper
import ru.otus.saturn33.movielist.presentation.scheduler.AlarmHelper

class PostponeBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val movieBundle = intent?.extras?.getBundle(AlarmHelper.MOVIE_BUNDLE_KEY)
        val movie = movieBundle?.getParcelable<MovieDTO>(AlarmHelper.MOVIE_KEY)
        movie?.let {
            NotificationHelper.postponeNotification(App.instance?.applicationContext!!, it)
        }
    }
}
