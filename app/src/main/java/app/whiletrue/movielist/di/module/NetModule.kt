package app.whiletrue.movielist.di.module

import app.whiletrue.movielist.data.network.LoggerInterceptor
import app.whiletrue.movielist.data.repository.MoviesRepository
import app.whiletrue.movielist.data.service.MovieDBService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class NetModule {

    @Provides
    fun provideInterceptor() : HttpLoggingInterceptor =
        HttpLoggingInterceptor(LoggerInterceptor()).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(2, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                chain.proceed(
                    chain.request()
                        .newBuilder()
                        .addHeader("Authorization", "Bearer ${MoviesRepository.API_TOKEN}")
                        .build()
                )
            }
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    fun provideRetrofit(client: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(MoviesRepository.BASE_API_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideNetworkService(retrofit: Retrofit): MovieDBService {
        return retrofit.create(MovieDBService::class.java)
    }
}
