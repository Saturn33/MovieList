package ru.otus.saturn33.movielist.data.repository

import ru.otus.saturn33.movielist.data.entity.MovieDTO

class MoviesRepository {
    private val cachedMovies = ArrayList<MovieDTO>()
    private val fakeMovies = ArrayList<MovieDTO>()

    private val favMovies: MutableSet<Int> = mutableSetOf()
    private val checkedMovies: MutableSet<Int> = mutableSetOf()

    var page = 0

    val cachedOrFakeMovies: List<MovieDTO>
        get() = if (cachedMovies.size > 0) cachedMovies else fakeMovies

    fun addToCache(movies: List<MovieDTO>) {
        cachedMovies.addAll(movies)
    }

    fun clearCache() {
        cachedMovies.clear()
    }

    fun inFav(id: Int) = favMovies.contains(id)
    fun changeFav(id: Int) = if (favMovies.contains(id)) favMovies.remove(id) else favMovies.add(id)

    fun inChecked(id: Int) = checkedMovies.contains(id)
    fun addToChecked(id: Int) = checkedMovies.add(id)

    init {
        fakeMovies.add(
            MovieDTO(
                19404,
                "Dilwale Dulhania Le Jayenge",
                "Raj is a rich, carefree, happy-go-lucky second generation NRI. Simran is the daughter of Chaudhary Baldev Singh, who in spite of being an NRI is very strict about adherence to Indian values. Simran has left for India to be married to her childhood fiancé. Raj leaves for India with a mission at his hands, to claim his lady love under the noses of her whole family. Thus begins a saga.",
                8.8,
                "/2CAL2433ZeIihfX1Hb2139CX0pW.jpg"
            )
        )
        fakeMovies.add(
            MovieDTO(
                278,
                "The Shawshank Redemption",
                "Framed in the 1940s for the double murder of his wife and her lover, upstanding banker Andy Dufresne begins a new life at the Shawshank prison, where he puts his accounting skills to work for an amoral warden. During his long stretch in prison, Dufresne comes to be admired by the other inmates -- including an older prisoner named Red -- for his integrity and unquenchable sense of hope.",
                8.7,
                "/9O7gLzmreU0nGkIB6K3BsJbzvNv.jpg"
            )
        )
        fakeMovies.add(
            MovieDTO(
                238,
                "Dilwale Dulhania Le Jayenge",
                "Spanning the years 1945 to 1955, a chronicle of the fictional Italian-American Corleone crime family. When organized crime family patriarch, Vito Corleone barely survives an attempt on his life, his youngest son, Michael steps in to take care of the would-be killers, launching a campaign of bloody revenge.",
                8.7,
                "/rPdtLWNsZmAtoZl9PK7S2wE3qiS.jpg"
            )
        )
        fakeMovies.add(
            MovieDTO(
                496243,
                "Parasite",
                "All unemployed, Ki-taek\\u0027s family takes peculiar interest in the wealthy and glamorous Parks for their livelihood until they get entangled in an unexpected incident.",
                8.6,
                "/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg"
            )
        )
    }

    companion object {
        const val BASE_API_URL = "https://api.themoviedb.org/3/"
        const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w500"
        //        const val API_KEY = "8c6675a3a4d5a4ac8fe70fc33031359a"
        const val API_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4YzY2NzVhM2E0ZDVhNGFjOGZlNzBmYzMzMDMxMzU5YSIsInN1YiI6IjVlNDAwMGU0NDE0NjVjMDAxNmNiNTljMCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.BsJERNWt56r3jKX_JsTdD7g6AIUD-IdGfIwx-teIRh8"
    }
}
