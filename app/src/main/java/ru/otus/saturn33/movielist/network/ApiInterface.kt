package ru.otus.saturn33.movielist.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.otus.saturn33.movielist.data.MoviesResponse


interface ApiInterface {
    @GET("movie/top_rated")
    fun getTopRatedMovies(@Query("page") page: Int? = 1): Call<MoviesResponse>?

    @GET("movie/{id}")
    fun getMovieDetails(@Path("id") id: Int): Call<MoviesResponse>?
}