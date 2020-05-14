package app.whiletrue.movielist.di.component

import app.whiletrue.movielist.App
import app.whiletrue.movielist.di.module.AppModule
import app.whiletrue.movielist.di.module.DBModule
import app.whiletrue.movielist.di.module.NetModule
import app.whiletrue.movielist.domain.MoviesInteractor
import app.whiletrue.movielist.presentation.viewmodel.MovieListViewModelFactory
import app.whiletrue.movielist.service.FirebaseMessageService
import dagger.Component
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