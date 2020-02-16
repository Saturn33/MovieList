package ru.otus.saturn33.movielist.data

import ru.otus.saturn33.movielist.data.entity.MovieDTO

object Storage {
    val movies: MutableList<MovieDTO> = arrayListOf()
    var lastSeenPosition = 0
}
