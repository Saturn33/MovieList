package ru.otus.saturn33.movielist.data.service

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.otus.saturn33.movielist.data.entity.MoviesResponse


interface MovieDBService {
    @GET("movie/top_rated")
    fun getTopRatedMovies(@Query("page") page: Int? = 1): Observable<MoviesResponse>

    @GET("movie/{id}")
    fun getMovieDetails(@Path("id") id: Int): Observable<MoviesResponse>
}