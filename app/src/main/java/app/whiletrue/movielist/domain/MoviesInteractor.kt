package app.whiletrue.movielist.domain

import android.annotation.SuppressLint
import android.content.Context
import app.whiletrue.movielist.App
import app.whiletrue.movielist.data.entity.MovieDTO
import app.whiletrue.movielist.data.repository.MoviesRepository
import app.whiletrue.movielist.data.repository.MoviesRepository.Companion.MOVIES_CACHE_TTL
import app.whiletrue.movielist.data.repository.MoviesRepository.Companion.MOVIES_LAST_ACCESS
import app.whiletrue.movielist.data.repository.MoviesRepository.Companion.SHARED_NAME
import app.whiletrue.movielist.data.service.MovieDBService
import app.whiletrue.movielist.presentation.scheduler.AlarmHelper
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class MoviesInteractor @Inject constructor(): IMoviesInteractor {
    init {
        App.instance!!.appComponent.inject(this)
    }
    @Inject
    lateinit var movieDBService: MovieDBService
    @Inject
    lateinit var moviesRepository: MoviesRepository

    @SuppressLint("CheckResult")
    fun getTopRatedMovies(page: Int = 1): Single<List<MovieDTO>> {
        val pref = App.instance!!.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE)
        val lastAccess = pref.getLong(MOVIES_LAST_ACCESS, 0)
        val now = Date().time
        val ret = Single.create<List<MovieDTO>> { emitter ->
            if (now - lastAccess > MOVIES_CACHE_TTL) {
                movieDBService.getTopRatedMovies(page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response ->
                        if (response.page > moviesRepository.page)
                            moviesRepository.page = response.page
                        response.results?.let {
                            moviesRepository.addToCache(it)?.let { single ->
                                single.subscribe { _ ->
                                    moviesRepository.cachedMovies?.let { single ->
                                        single
                                            .subscribeOn(Schedulers.io())
                                            .subscribe { data ->
                                                emitter.onSuccess(data)
                                            }
                                    }
                                }
                            }
                        }


                    }, { error ->
                        emitter.onError(Throwable(error.message ?: "unknown"))
                    })
            } else {
                moviesRepository.cachedMovies?.let { single ->
                    single
                        .subscribeOn(Schedulers.io())
                        .subscribe { data -> emitter.onSuccess(data) }
                }
            }
        }
        return ret
    }

    fun getExact(movieId: Int): Single<MovieDTO?>? {
        return moviesRepository.getExact(movieId)
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
}
