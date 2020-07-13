package app.whiletrue.movielist.data.service

import app.whiletrue.movielist.data.entity.MoviesResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MovieDBService {
    @GET("movie/top_rated")
    fun getTopRatedMovies(@Query("page") page: Int? = 1): Observable<MoviesResponse>

    @GET("movie/{id}")
    fun getMovieDetails(@Path("id") id: Int): Observable<MoviesResponse>
}