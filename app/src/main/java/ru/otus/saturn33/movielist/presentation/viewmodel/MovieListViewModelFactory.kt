package ru.otus.saturn33.movielist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.otus.saturn33.movielist.App
import javax.inject.Inject

class MovieListViewModelFactory : ViewModelProvider.Factory {
    @Inject
    lateinit var movieListViewModel: MovieListViewModel

    init {
        App.instance!!.appComponent.inject(this)
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass == MovieListViewModel::class.java) {
            @Suppress("UNCHECKED_CAST")
            return movieListViewModel as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
