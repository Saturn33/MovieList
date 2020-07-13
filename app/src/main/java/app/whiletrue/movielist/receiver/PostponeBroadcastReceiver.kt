package app.whiletrue.movielist.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import app.whiletrue.movielist.App
import app.whiletrue.movielist.data.entity.MovieDTO
import app.whiletrue.movielist.presentation.notification.NotificationHelper
import app.whiletrue.movielist.presentation.scheduler.AlarmHelper

class PostponeBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val movieBundle = intent?.extras?.getBundle(AlarmHelper.MOVIE_BUNDLE_KEY)
        val movie = movieBundle?.getParcelable<MovieDTO>(AlarmHelper.MOVIE_KEY)
        movie?.let {
            NotificationHelper.postponeNotification(App.instance?.applicationContext!!, it)
        }
    }
}
