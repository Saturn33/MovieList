package app.whiletrue.movielist.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import app.whiletrue.movielist.data.entity.*

@Database(entities = [MovieDTO::class, FavDTO::class, PostponeDTO::class], version = 3)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDAO
    abstract fun favoritesDao(): FavDAO
    abstract fun postponeDao(): PostponeDAO
}
