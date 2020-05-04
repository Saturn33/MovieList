package ru.otus.saturn33.movielist.di.component

import dagger.Component
import ru.otus.saturn33.movielist.App
import ru.otus.saturn33.movielist.di.module.AppModule
import ru.otus.saturn33.movielist.di.module.DBModule
import ru.otus.saturn33.movielist.di.module.NetModule
import ru.otus.saturn33.movielist.domain.MoviesInteractor
import ru.otus.saturn33.movielist.presentation.viewmodel.MovieListViewModelFactory
import ru.otus.saturn33.movielist.service.FirebaseMessageService
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetModule::class, DBModule::class])
interface AppComponent {
    fun inject(app: App)
    fun inject(moviesInteractor: MoviesInteractor)
    fun inject(movieListVMFactory: MovieListViewModelFactory)
    fun inject(firebaseMessageService: FirebaseMessageService)

//    fun moviesRepository(): IMoviesRepository
//    fun moviesInteractor(): IMoviesInteractor
}