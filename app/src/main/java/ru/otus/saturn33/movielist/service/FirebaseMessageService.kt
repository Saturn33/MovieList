package ru.otus.saturn33.movielist.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ru.otus.saturn33.movielist.App
import ru.otus.saturn33.movielist.presentation.notification.NotificationHelper

class FirebaseMessageService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        Log.d(TAG, "New token: $token")
        println(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val movieId = message.data["movieId"]?.toInt() ?: 0
        Log.d(TAG, "MovieId: $movieId")
        val movie = App.instance!!.moviesInteractor.getExact(movieId)
        Log.d(TAG, movie.toString())
        if (movie != null)
            NotificationHelper.detailedMovieNotification(App.instance!!.applicationContext, movie, message.data["title"], message.data["text"])
    }

    companion object {
        const val TAG = "FCM!"
    }
}
