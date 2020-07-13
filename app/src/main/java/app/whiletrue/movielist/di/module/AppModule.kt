package app.whiletrue.movielist.di.module

import android.app.Application
import app.whiletrue.movielist.domain.IMoviesInteractor
import app.whiletrue.movielist.domain.MoviesInteractor
import app.whiletrue.movielist.presentation.viewmodel.MovieListViewModel
import dagger.Module
import dagger.Provides
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

    @Provides
    @Singleton
    fun provideMovieListViewModel(
        application: Application,
        interactor: MoviesInteractor
    ): MovieListViewModel {
        return MovieListViewModel(application, interactor)
    }
}
