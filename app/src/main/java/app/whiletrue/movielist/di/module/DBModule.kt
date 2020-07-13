package app.whiletrue.movielist.di.module

import android.app.Application
import androidx.room.Room
import app.whiletrue.movielist.data.database.MoviesDatabase
import app.whiletrue.movielist.data.entity.FavDAO
import app.whiletrue.movielist.data.entity.MovieDAO
import app.whiletrue.movielist.data.entity.PostponeDAO
import app.whiletrue.movielist.data.repository.IMoviesRepository
import app.whiletrue.movielist.data.repository.MoviesRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DBModule(val app: Application) {
    var db: MoviesDatabase? = null

    @Singleton
    @Provides
    fun provideRoomDatabase(): MoviesDatabase {
        if (db == null) {
            synchronized(MoviesDatabase::class) {
                db = Room.databaseBuilder(
                    app,
                    MoviesDatabase::class.java, "movies.db"
                )
//                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
        }
        return db!!
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
