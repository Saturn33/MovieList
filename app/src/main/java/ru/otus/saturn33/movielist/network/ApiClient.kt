package ru.otus.saturn33.movielist.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient : BaseApi() {
    var service: ApiInterface

    init {
        service = this.getClient().create(ApiInterface::class.java)
    }


    private fun getClient(): Retrofit {
        val interceptor = HttpLoggingInterceptor(LoggerInterceptor()).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                chain.proceed(
                    chain.request()
                        .newBuilder()
                        .addHeader("Authorization", "Bearer $API_TOKEN")
                        .build()
                )
            }
            .addInterceptor(interceptor)
            .build()

        return Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
