package ru.otus.saturn33.movielist

import android.app.Application
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.otus.saturn33.movielist.data.database.Db
import ru.otus.saturn33.movielist.data.network.LoggerInterceptor
import ru.otus.saturn33.movielist.data.repository.MoviesRepository
import ru.otus.saturn33.movielist.data.service.MovieDBService
import ru.otus.saturn33.movielist.domain.MoviesInteractor
import java.util.concurrent.TimeUnit

class App : Application() {
    lateinit var movieDBService: MovieDBService
    lateinit var moviesInteractor: MoviesInteractor
    lateinit var moviesRepository: MoviesRepository

    override fun onCreate() {
        super.onCreate()

        instance = this

        initRetrofit()
        initRoom()
        initInteractor()
    }

    private fun initRoom() {
        val movieDao = Db.getInstance(this)?.movieDao()
        val favDao = Db.getInstance(this)?.favoritesDao()
        val postponeDao = Db.getInstance(this)?.postponeDao()
        moviesRepository = MoviesRepository(movieDao, favDao, postponeDao)
    }

    private fun initInteractor() {
        moviesInteractor = MoviesInteractor(movieDBService, moviesRepository)
    }

    private fun initRetrofit() {
        val interceptor = HttpLoggingInterceptor(LoggerInterceptor()).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .connectTimeout(2, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                chain.proceed(
                    chain.request()
                        .newBuilder()
                        .addHeader("Authorization", "Bearer ${MoviesRepository.API_TOKEN}")
                        .build()
                )
            }
            .addInterceptor(interceptor)
            .build()

        movieDBService = Retrofit.Builder()
            .client(client)
            .baseUrl(MoviesRepository.BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieDBService::class.java)

    }

    companion object {
        var instance: App? = null
            private set
    }

}