package ru.otus.saturn33.movielist.service

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ru.otus.saturn33.movielist.App
import ru.otus.saturn33.movielist.domain.MoviesInteractor
import ru.otus.saturn33.movielist.presentation.notification.NotificationHelper
import javax.inject.Inject

class FirebaseMessageService : FirebaseMessagingService() {
    @Inject
    lateinit var moviesInteractor: MoviesInteractor
    override fun onNewToken(token: String) {
        Log.d(TAG, "New token: $token")
        println(token)
    }

    @SuppressLint("CheckResult")
    override fun onMessageReceived(message: RemoteMessage) {
        val movieId = message.data["movieId"]?.toInt() ?: 0
        Log.d(TAG, "MovieId: $movieId")
        val sMovie = moviesInteractor.getExact(movieId)
        sMovie?.subscribe { movie ->
            if (movie == null) return@subscribe
            movie.imageURL = if (movie.imagePath == null) null else "${moviesInteractor.getBaseImageURL()}${movie.imagePath}"
            Log.d(TAG, movie.toString())
            NotificationHelper.detailedMovieNotification(App.instance!!.applicationContext, movie, message.data["title"], message.data["text"])
        }
    }

    companion object {
        const val TAG = "FCM!"
    }
}
