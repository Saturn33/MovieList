package app.whiletrue.movielist.presentation.scheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import app.whiletrue.movielist.data.entity.MovieDTO
import app.whiletrue.movielist.receiver.PostponeBroadcastReceiver
import java.util.*

object AlarmHelper {
    const val MOVIE_KEY = "movie"
    const val MOVIE_BUNDLE_KEY = "movieBundle"

    fun addPostponeMovieAlarm(context: Context, movie: MovieDTO, date: Date) {
        val intent = Intent(context, PostponeBroadcastReceiver::class.java).apply {
            val movieBundle = Bundle().apply {
                putParcelable(MOVIE_KEY, movie)
            }
            putExtra(MOVIE_BUNDLE_KEY, movieBundle)
        }
        val pendIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val mgr = context.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        //получение уведомления примерно через секунду после установки, для тестов
//        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, pendIntent)

        //получение уведомления в указанную дату в 10 часов
        //если указали дату меньше, чем текущая, удаляем аларм, иначе устанавливаем
        if (date.time < System.currentTimeMillis()) {
            mgr.cancel(pendIntent)
        }
        else {
            mgr.set(AlarmManager.RTC, date.time, pendIntent)
        }
    }
}
