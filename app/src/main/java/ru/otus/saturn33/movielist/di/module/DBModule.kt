package ru.otus.saturn33.movielist.di.module

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.otus.saturn33.movielist.data.database.MoviesDatabase
import ru.otus.saturn33.movielist.data.entity.FavDAO
import ru.otus.saturn33.movielist.data.entity.MovieDAO
import ru.otus.saturn33.movielist.data.entity.PostponeDAO
import ru.otus.saturn33.movielist.data.repository.IMoviesRepository
import ru.otus.saturn33.movielist.data.repository.MoviesRepository
import javax.inject.Singleton

@Module
class DBModule(app: Application) {
    private val db: MoviesDatabase = Room.databaseBuilder(
        app,
        MoviesDatabase::class.java, "movies.db"
    )
//                    .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideRoomDatabase(): MoviesDatabase {
        return db
    }

    @Singleton
    @Provides
    fun provideMovieDAO(db: MoviesDatabase): MovieDAO {
        return db.movieDao()
    }

    @Singleton
    @Provides
    fun provideFavDAO(db: MoviesDatabase): FavDAO {
        return db.favoritesDao()
    }

    @Singleton
    @Provides
    fun providePostponeDAO(db: MoviesDatabase): PostponeDAO {
        return db.postponeDao()
    }

    @Singleton
    @Provides
    fun moviesRepository(
        movieDAO: MovieDAO,
        favDAO: FavDAO,
        postponeDAO: PostponeDAO
    ): IMoviesRepository {
        return MoviesRepository(movieDAO, favDAO, postponeDAO)
    }
}
