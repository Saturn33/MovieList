package ru.otus.saturn33.movielist.data.database

import android.content.Context
import androidx.room.Room

object Db {
    private var instance: MoviesDatabase? = null

    fun getInstance(context: Context): MoviesDatabase? {
        if (instance == null) {
            synchronized(MoviesDatabase::class) {

                instance = Room.databaseBuilder(
                    context,
                    MoviesDatabase::class.java, "movies.db"
                )
//                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
        }
        return instance
    }

    fun destroyInstance() {
        instance?.close()
        instance = null
    }
}
