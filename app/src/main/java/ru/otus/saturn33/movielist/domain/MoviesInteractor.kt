package ru.otus.saturn33.movielist.domain

import android.content.Context
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.otus.saturn33.movielist.App
import ru.otus.saturn33.movielist.data.entity.MovieDTO
import ru.otus.saturn33.movielist.data.entity.MoviesResponse
import ru.otus.saturn33.movielist.data.repository.MoviesRepository
import ru.otus.saturn33.movielist.data.repository.MoviesRepository.Companion.MOVIES_CACHE_TTL
import ru.otus.saturn33.movielist.data.repository.MoviesRepository.Companion.MOVIES_LAST_ACCESS
import ru.otus.saturn33.movielist.data.repository.MoviesRepository.Companion.SHARED_NAME
import ru.otus.saturn33.movielist.data.service.MovieDBService
import java.util.*
import java.util.concurrent.Executors

class MoviesInteractor(
    private val movieDBService: MovieDBService,
    private val moviesRepository: MoviesRepository
) {

    fun getTopRatedMovies(page: Int = 1, callback: GetMoviesCallback) {
        val pref = App.instance!!.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE)
        val lastAccess = pref.getLong(MOVIES_LAST_ACCESS, 0)
        val now = Date().time

        if (now - lastAccess > MOVIES_CACHE_TTL) {
            movieDBService.getTopRatedMovies(page).enqueue(object : Callback<MoviesResponse> {
                override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                    callback.onError(t.message ?: "Unknown error")
                }

                override fun onResponse(
                    call: Call<MoviesResponse>,
                    response: Response<MoviesResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()!!.results?.let { moviesRepository.addToCache(it) }
                        if (response.body()!!.page > moviesRepository.page)
                            moviesRepository.page = response.body()!!.page
                        Executors.newSingleThreadExecutor().submit {
                            callback.onSuccess(moviesRepository.cachedMovies)
                        }
                    } else {
                        callback.onError(response.code().toString() + " " + response.errorBody())
                    }
                }

            })
        } else {
            Executors.newSingleThreadExecutor().submit {
                callback.onSuccess(moviesRepository.cachedMovies)
            }
        }
    }

    fun checkInFav(movie: MovieDTO) = moviesRepository.inFav(movie.id)
    fun changeFav(movie: MovieDTO) = moviesRepository.changeFav(movie.id)
    fun checkPostponed(movie: MovieDTO) = moviesRepository.isPostponed(movie.id)
    fun setPostpone(movie: MovieDTO, date: Date) {
        moviesRepository.setPostpone(movie.id, date)
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