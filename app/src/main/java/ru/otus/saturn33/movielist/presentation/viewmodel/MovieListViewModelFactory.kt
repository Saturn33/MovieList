package ru.otus.saturn33.movielist.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MovieListViewModelFactory(private val application: Application, private val message: String?) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass == MovieListViewModel::class.java) {
            @Suppress("UNCHECKED_CAST")
            return MovieListViewModel(application, message) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
