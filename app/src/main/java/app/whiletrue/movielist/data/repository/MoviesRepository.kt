package app.whiletrue.movielist.data.repository

import android.annotation.SuppressLint
import android.content.Context
import app.whiletrue.movielist.App
import app.whiletrue.movielist.data.entity.*
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

class MoviesRepository @Inject constructor(private val moviesDao: MovieDAO, private val favDao: FavDAO, private val postponeDao: PostponeDAO): IMoviesRepository {
    private val favMovies: MutableSet<Int> = mutableSetOf()
    private val checkedMovies: MutableSet<Int> = mutableSetOf()
    private val postponedMovies: HashMap<Int, Long> = HashMap()
    private val pref = App.instance!!.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE)

    var page = 0

    val cachedMovies: Single<List<MovieDTO>>?
        get() = moviesDao.getAll()

    fun getExact(movieId: Int) = moviesDao.read(movieId)

    @SuppressLint("CheckResult")
    fun addToCache(movies: List<MovieDTO>): Single<List<Long>?>? {
        val single = moviesDao
            .create(movies)
            .subscribeOn(Schedulers.io())
        single
            .subscribe { _ ->
                pref.edit().apply {
                    this.putLong(MOVIES_LAST_ACCESS, Date().time)
                    this.apply()
                }
            }
        return single
    }

    @SuppressLint("CheckResult")
    fun clearCache() {
        moviesDao
            .clear()
            .subscribeOn(Schedulers.io())
            .subscribe {
                pref.edit().apply {
                    this.remove(MOVIES_LAST_ACCESS)
                    this.apply()
                }
            }
    }

    fun inFav(id: Int) = favMovies.contains(id)
    fun changeFav(id: Int) {
        if (favMovies.contains(id)) {
            favMovies.remove(id)
            favDao.delete(FavDTO(id))
                .subscribeOn(Schedulers.io())
                .subscribe()
        } else {
            favMovies.add(id)
            favDao.add(FavDTO(id))
                .subscribeOn(Schedulers.io())
                .subscribe()
        }
    }

    fun isPostponed(id: Int): Pair<Boolean, Long?> = (System.currentTimeMillis() < postponedMovies[id] ?: 0) to postponedMovies[id]
    fun setPostpone(id: Int, date: Date) {
        postponedMovies[id] = date.time
        postponeDao.add(PostponeDTO(id, date.time))
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun inChecked(id: Int) = checkedMovies.contains(id)
    fun addToChecked(id: Int) = checkedMovies.add(id)

    init {
        favDao
            .getAll()
            .subscribeOn(Schedulers.io())
            .subscribe { data ->
                data.forEach { favMovies.add(it.movieId) }
            }


        postponeDao
            .getAll()
            .subscribeOn(Schedulers.io())
            .subscribe { data ->
                data.forEach { postponedMovies[it.movieId] = it.date }
            }
    }

    companion object {
        const val BASE_API_URL = "https://api.themoviedb.org/3/"
        const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w500"
        //        const val API_KEY = "8c6675a3a4d5a4ac8fe70fc33031359a"
        const val API_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4YzY2NzVhM2E0ZDVhNGFjOGZlNzBmYzMzMDMxMzU5YSIsInN1YiI6IjVlNDAwMGU0NDE0NjVjMDAxNmNiNTljMCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.BsJERNWt56r3jKX_JsTdD7g6AIUD-IdGfIwx-teIRh8"

        const val MOVIES_CACHE_TTL = 20 * 60 * 1000//milliseconds
        const val MOVIES_LAST_ACCESS = "last_access"
        const val SHARED_NAME = "movies_config"
    }
}
