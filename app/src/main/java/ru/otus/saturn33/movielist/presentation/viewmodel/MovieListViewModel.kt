package ru.otus.saturn33.movielist.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.otus.saturn33.movielist.App
import ru.otus.saturn33.movielist.data.entity.MovieDTO
import ru.otus.saturn33.movielist.domain.MoviesInteractor

class MovieListViewModel(message: String?) : ViewModel() {
    private val errorLiveData = MutableLiveData<String?>()
    private val toastLiveData = MutableLiveData<String?>()
    private val moviesLiveData = MutableLiveData<List<MovieDTO>>()
    private val selectedMovieLiveData = MutableLiveData<MovieDTO>()
    private val swipeRefresherLiveData: MutableLiveData<Boolean> = MutableLiveData(false)
    private val lastSeenPositionLiveData: MutableLiveData<Int> = MutableLiveData(0)
    private val inUpdateLiveData: MutableLiveData<Boolean> = MutableLiveData(false)
    private val isFirstAddLiveData: MutableLiveData<Boolean> = MutableLiveData(true)
    private val moviesInteractor = App.instance!!.moviesInteractor

    init {
        if (message != null) {
            toastLiveData.postValue(message)
        }
    }

    val error: LiveData<String?>
        get() = errorLiveData

    val movies: LiveData<List<MovieDTO>>
        get() = Transformations.map(moviesLiveData, ::addInfo)

    val moviesFav: LiveData<List<MovieDTO>>
        get() = Transformations.map(moviesLiveData, ::filterFav)

    val selectedMovie: LiveData<MovieDTO>
        get() = selectedMovieLiveData

    val swipeRefresher: LiveData<Boolean>
        get() = swipeRefresherLiveData

    val lastSeenPosition: LiveData<Int>
        get() = lastSeenPositionLiveData

    val inUpdate: LiveData<Boolean>
        get() = inUpdateLiveData

    val isFirstAdd: LiveData<Boolean>
        get() = isFirstAddLiveData

    val toast: LiveData<String?>
        get() = toastLiveData

    private fun addInfo(movies: List<MovieDTO>): List<MovieDTO> {
        for (movie in movies) {
            movie.inFav = moviesInteractor.checkInFav(movie)
            movie.checked = moviesInteractor.checkInChecked(movie)
            movie.imageURL =
                if (movie.imagePath == null) null else "${moviesInteractor.getBaseImageURL()}${movie.imagePath}"
        }

        return movies
    }

    private fun filterFav(movies: List<MovieDTO>): List<MovieDTO> {
        return addInfo(movies).filter { it.inFav }
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
        swipeRefresherLiveData.postValue(true)
    }

    private fun onClear() {
        moviesInteractor.setCurrentPage(0)
        moviesInteractor.clearCache()
        moviesLiveData.postValue(listOf())
        isFirstAddLiveData.postValue(true)
    }

    fun onErrorHandled() {
        errorLiveData.postValue(null)
    }

    fun onToastHandled() {
        toastLiveData.postValue(null)
    }

    fun onNextPageRequest() {
        if (inUpdateLiveData.value == true) return
        inUpdateLiveData.postValue(true)
        moviesInteractor.getTopRatedMovies(moviesInteractor.getCurrentPage() + 1,
            object : MoviesInteractor.GetMoviesCallback {
                override fun onSuccess(movies: List<MovieDTO>) {
                    moviesLiveData.postValue(movies)
                    swipeRefresherLiveData.postValue(false)
                    inUpdateLiveData.postValue(false)
                }

                override fun onError(error: String) {
                    errorLiveData.postValue(error)
                    swipeRefresherLiveData.postValue(false)
                    inUpdateLiveData.postValue(false)
                }
            })
    }

    fun setLastSeenPosition(position: Int) {
        lastSeenPositionLiveData.postValue(position)
    }

    fun onFirstPageScrolled() {
        isFirstAddLiveData.postValue(false)
    }

    fun getCurrentPage() = moviesInteractor.getCurrentPage()

}