package ru.otus.saturn33.movielist.network

open class BaseApi {
    companion object {
        const val BASE_API_URL = "https://api.themoviedb.org/3/"
        const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w500"
//        const val API_KEY = "8c6675a3a4d5a4ac8fe70fc33031359a"
        const val API_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4YzY2NzVhM2E0ZDVhNGFjOGZlNzBmYzMzMDMxMzU5YSIsInN1YiI6IjVlNDAwMGU0NDE0NjVjMDAxNmNiNTljMCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.BsJERNWt56r3jKX_JsTdD7g6AIUD-IdGfIwx-teIRh8"
        const val LOAD_NEXT_PAGE_BEFORE_LAST_ELEMENTS = 5
    }
}