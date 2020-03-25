package ru.otus.saturn33.movielist.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.otus.saturn33.movielist.data.entity.*

@Database(entities = [MovieDTO::class, FavDTO::class, PostponeDTO::class], version = 3)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDAO
    abstract fun favoritesDao(): FavDAO
    abstract fun postponeDao(): PostponeDAO
}
