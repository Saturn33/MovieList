package app.whiletrue.movielist.service

import android.annotation.SuppressLint
import android.util.Log
import app.whiletrue.movielist.App
import app.whiletrue.movielist.BuildConfig
import app.whiletrue.movielist.domain.MoviesInteractor
import app.whiletrue.movielist.presentation.notification.NotificationHelper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import javax.inject.Inject

class FirebaseMessageService : FirebaseMessagingService() {
    @Inject
    lateinit var moviesInteractor: MoviesInteractor

    init {
        App.instance!!.appComponent.inject(this)
    }

    override fun onNewToken(token: String) {
        if (!BuildConfig.DEBUG) {
            Log.d(TAG, "New token: $token")
            println(token)
        }
    }

    @SuppressLint("CheckResult")
    override fun onMessageReceived(message: RemoteMessage) {
        val movieId = message.data["movieId"]?.toInt() ?: 0
        if (!BuildConfig.DEBUG) {
            Log.d(TAG, "MovieId: $movieId")
        }
        val sMovie = moviesInteractor.getExact(movieId)
        sMovie?.subscribe { movie ->
            if (movie == null) return@subscribe
            movie.imageURL = if (movie.imagePath == null) null else "${moviesInteractor.getBaseImageURL()}${movie.imagePath}"
            if (!BuildConfig.DEBUG) {
                Log.d(TAG, movie.toString())
            }
            NotificationHelper.detailedMovieNotification(App.instance!!.applicationContext, movie, message.data["title"], message.data["text"])
        }
    }

    companion object {
        const val TAG = "FCM!"
    }
}
