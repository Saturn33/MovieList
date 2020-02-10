package ru.otus.saturn33.movielist.data

object Storage {
    val movies: MutableList<MovieDTO> = arrayListOf()
    val favMovies: MutableSet<Int> = mutableSetOf()
    val checkedMovies: MutableSet<Int> = mutableSetOf()
    var page = 0
}
