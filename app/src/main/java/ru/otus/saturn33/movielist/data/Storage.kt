package ru.otus.saturn33.movielist.data

import ru.otus.saturn33.movielist.R

object Storage {
    val movies: List<MovieDTO> = arrayListOf(
        MovieDTO(
            "Побег из Шоушенка",
            "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.",
            R.drawable.img0
        ),
        MovieDTO(
            "Крёстный отец",
            "The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.",
            R.drawable.img1
        ),
        MovieDTO(
            "Криминальное чтиво",
            "The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits intertwine in four tales of violence and redemption.",
            R.drawable.img2
        ),
        MovieDTO(
            "Начало",
            "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.",
            R.drawable.img3
        )
    )
}
