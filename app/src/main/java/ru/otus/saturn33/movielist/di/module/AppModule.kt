package ru.otus.saturn33.movielist.di.module

import android.app.Application
import dagger.Module
import dagger.Provides
import ru.otus.saturn33.movielist.domain.IMoviesInteractor
import ru.otus.saturn33.movielist.domain.MoviesInteractor
import javax.inject.Singleton

@Module
class AppModule(var application: Application) {
    @Provides
    @Singleton
    fun provideApplication(): Application = application

    @Provides
    @Singleton
    fun provideInteractor(): IMoviesInteractor {
        return MoviesInteractor()
    }
}
