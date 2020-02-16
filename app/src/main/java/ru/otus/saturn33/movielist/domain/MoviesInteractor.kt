package ru.otus.saturn33.movielist.domain

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.otus.saturn33.movielist.data.entity.MovieDTO
import ru.otus.saturn33.movielist.data.entity.MoviesResponse
import ru.otus.saturn33.movielist.data.repository.MoviesRepository
import ru.otus.saturn33.movielist.data.service.MovieDBService

class MoviesInteractor(
    private val movieDBService: MovieDBService,
    private val moviesRepository: MoviesRepository
) {

    fun getTopRatedMovies(page: Int = 1, callback: GetMoviesCallback) {
        movieDBService.getTopRatedMovies(page).enqueue(object : Callback<MoviesResponse> {
            override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
//                moviesRepository.addToCache(moviesRepository.cachedOrFakeMovies)
//                callback.onSuccess(moviesRepository.cachedOrFakeMovies) //TODO удалить
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
                    callback.onSuccess(moviesRepository.cachedOrFakeMovies)
                } else {
                    callback.onError(response.code().toString() + " " + response.errorBody())
                }
            }

        })
    }

    fun checkInFav(movie: MovieDTO) = moviesRepository.inFav(movie.id)
    fun changeFav(movie: MovieDTO) = moviesRepository.changeFav(movie.id)
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