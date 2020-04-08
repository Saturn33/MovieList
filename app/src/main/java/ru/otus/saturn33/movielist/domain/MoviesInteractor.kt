package ru.otus.saturn33.movielist.domain

import android.annotation.SuppressLint
import android.content.Context
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.otus.saturn33.movielist.App
import ru.otus.saturn33.movielist.data.entity.MovieDTO
import ru.otus.saturn33.movielist.data.repository.MoviesRepository
import ru.otus.saturn33.movielist.data.repository.MoviesRepository.Companion.MOVIES_CACHE_TTL
import ru.otus.saturn33.movielist.data.repository.MoviesRepository.Companion.MOVIES_LAST_ACCESS
import ru.otus.saturn33.movielist.data.repository.MoviesRepository.Companion.SHARED_NAME
import ru.otus.saturn33.movielist.data.service.MovieDBService
import ru.otus.saturn33.movielist.presentation.scheduler.AlarmHelper
import java.util.*

class MoviesInteractor(
    private val movieDBService: MovieDBService,
    private val moviesRepository: MoviesRepository
) {

    @SuppressLint("CheckResult")
    fun getTopRatedMovies(page: Int = 1, callback: GetMoviesCallback) {
        val pref = App.instance!!.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE)
        val lastAccess = pref.getLong(MOVIES_LAST_ACCESS, 0)
        val now = Date().time

        if (now - lastAccess > MOVIES_CACHE_TTL) {
            movieDBService.getTopRatedMovies(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    response.results?.let { moviesRepository.addToCache(it) }
                    if (response.page > moviesRepository.page)
                        moviesRepository.page = response.page
//                        Executors.newSingleThreadExecutor().submit {
                    callback.onSuccess(moviesRepository.cachedMovies)
//                        }

                }, { error ->
                    callback.onError(error.message ?: "unknown")
                })
        } else {
//            Executors.newSingleThreadExecutor().submit {
                callback.onSuccess(moviesRepository.cachedMovies)
//            }
        }
    }

    fun getExact(movieId: Int): MovieDTO? {
        val movie = moviesRepository.getExact(movieId) ?: return null
        movie.imageURL = if (movie.imagePath == null) null else "${getBaseImageURL()}${movie.imagePath}"
        return movie
    }

    fun checkInFav(movie: MovieDTO) = moviesRepository.inFav(movie.id)
    fun changeFav(movie: MovieDTO) = moviesRepository.changeFav(movie.id)
    fun checkPostponed(movie: MovieDTO) = moviesRepository.isPostponed(movie.id)
    fun setPostpone(movie: MovieDTO, date: Date) {
        moviesRepository.setPostpone(movie.id, date)
        AlarmHelper.addPostponeMovieAlarm(App.instance?.applicationContext!!, movie, date)
    }
    fun checkInChecked(movie: MovieDTO) = moviesRepository.inChecked(movie.id)
    fun addToChecked(movie: MovieDTO) = moviesRepository.addToChecked(movie.id)
    fun getCurrentPage() = moviesRepository.page
    fun setCurrentPage(page: Int) {
        moviesRepository.page = page
    }

    fun clearCache() {
        moviesRepository.clearCache()
    }

    fun getBaseImageURL(): String = MoviesRepository.BASE_IMAGE_URL

    interface GetMoviesCallback {
        fun onSuccess(movies: List<MovieDTO>)
        fun onError(error: String)
    }
}