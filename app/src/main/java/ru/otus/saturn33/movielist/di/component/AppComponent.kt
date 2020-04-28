package ru.otus.saturn33.movielist.di.component

import dagger.Component
import ru.otus.saturn33.movielist.App
import ru.otus.saturn33.movielist.data.repository.IMoviesRepository
import ru.otus.saturn33.movielist.di.module.AppModule
import ru.otus.saturn33.movielist.di.module.DBModule
import ru.otus.saturn33.movielist.di.module.NetModule
import ru.otus.saturn33.movielist.domain.IMoviesInteractor
import ru.otus.saturn33.movielist.domain.MoviesInteractor
import ru.otus.saturn33.movielist.presentation.viewmodel.MovieListViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetModule::class, DBModule::class])
interface AppComponent {
    fun inject(app: App)
    fun inject(moviesInteractor: MoviesInteractor)
    fun inject(movieListVM: MovieListViewModel)

    fun moviesRepository(): IMoviesRepository
    fun moviesInteractor(): IMoviesInteractor
}