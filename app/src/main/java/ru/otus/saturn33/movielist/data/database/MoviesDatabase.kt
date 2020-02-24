package ru.otus.saturn33.movielist.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.otus.saturn33.movielist.data.entity.FavDAO
import ru.otus.saturn33.movielist.data.entity.FavDTO
import ru.otus.saturn33.movielist.data.entity.MovieDAO
import ru.otus.saturn33.movielist.data.entity.MovieDTO

@Database(entities = [MovieDTO::class, FavDTO::class], version = 2)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDAO
    abstract fun favoritesDao(): FavDAO
}
