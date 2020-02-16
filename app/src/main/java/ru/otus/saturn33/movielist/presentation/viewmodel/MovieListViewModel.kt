package ru.otus.saturn33.movielist.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.otus.saturn33.movielist.App
import ru.otus.saturn33.movielist.data.entity.MovieDTO
import ru.otus.saturn33.movielist.domain.MoviesInteractor

class MovieListViewModel : ViewModel() {
    private val errorLiveData = MutableLiveData<String?>()
    private val moviesLiveData = MutableLiveData<List<MovieDTO>>()
    private val selectedMovieLiveData = MutableLiveData<MovieDTO>()
    private val swipeRefreshenerLiveData: MutableLiveData<Boolean> = MutableLiveData(false)
    var lastSeenPosition = 0
    var inUpdate = false

    private val moviesInteractor = App.instance!!.moviesInteractor

    val error: LiveData<String?>
        get() = errorLiveData

    val movies: LiveData<List<MovieDTO>>
        get() = Transformations.map(moviesLiveData, ::addInfo)

    val selectedMovie: LiveData<MovieDTO>
        get() = selectedMovieLiveData

    val swipeRefreshener: LiveData<Boolean>
        get() = swipeRefreshenerLiveData


    private fun addInfo(movies: List<MovieDTO>): List<MovieDTO> {
        for (movie in movies) {
            movie.inFav = moviesInteractor.checkInFav(movie)
            movie.checked = moviesInteractor.checkInChecked(movie)
            movie.imageURL =
                if (movie.imagePath == null) null else "${moviesInteractor.getBaseImageURL()}${movie.imagePath}"
        }

        return movies
    }

    fun onMovieSelect(movie: MovieDTO) {
        moviesInteractor.addToChecked(movie)
        moviesLiveData.postValue(moviesLiveData.value)
        selectedMovieLiveData.postValue(movie)
    }

    fun onMovieLike(movie: MovieDTO) {
        moviesInteractor.changeFav(movie)
        moviesLiveData.postValue(moviesLiveData.value)
    }

    fun onRefresh() {
        onClear()
        swipeRefreshenerLiveData.postValue(true)
    }

    fun onClear() {
        moviesInteractor.setCurrentPage(0)
        moviesInteractor.clearCache()
        moviesLiveData.postValue(mutableListOf())
    }

    fun onNextPageRequest() {
        if (inUpdate) return
        inUpdate = true
        moviesInteractor.getTopRatedMovies(moviesInteractor.getCurrentPage() + 1,
            object : MoviesInteractor.GetMoviesCallback {
                override fun onSuccess(movies: List<MovieDTO>) {
                    moviesLiveData.postValue(movies)
                    swipeRefreshenerLiveData.postValue(false)
                    inUpdate = false
                }

                override fun onError(error: String) {
                    errorLiveData.postValue(error)
                    swipeRefreshenerLiveData.postValue(false)
                    inUpdate = false
                }
            })
    }
}