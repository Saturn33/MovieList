package ru.otus.saturn33.movielist.di.module

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.otus.saturn33.movielist.data.network.LoggerInterceptor
import ru.otus.saturn33.movielist.data.repository.MoviesRepository
import ru.otus.saturn33.movielist.data.service.MovieDBService
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
        val client = OkHttpClient.Builder()
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

        return client
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
