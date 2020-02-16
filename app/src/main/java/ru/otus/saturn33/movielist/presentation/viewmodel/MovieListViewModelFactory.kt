package ru.otus.saturn33.movielist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MovieListViewModelFactory(private val message: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass == MovieListViewModel::class.java) {
            @Suppress("UNCHECKED_CAST")
            return MovieListViewModel(message) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
