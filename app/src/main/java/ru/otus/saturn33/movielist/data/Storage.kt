package ru.otus.saturn33.movielist.data

import ru.otus.saturn33.movielist.R

object Storage {
    val movies: List<MovieDTO> = arrayListOf(
        MovieDTO(
            "Побег из Шоушенка",
            "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.",
            0
        ),
        MovieDTO(
            "Крёстный отец",
            "The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.",
            1
        ),
        MovieDTO(
            "Криминальное чтиво",
            "The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits intertwine in four tales of violence and redemption.",
            2
        ),
        MovieDTO(
            "Начало",
            "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.",
            3
        )
    )

    fun getButtonId(i: Int): Int = when (i) {
        0 -> R.id.button0
        1 -> R.id.button1
        2 -> R.id.button2
        3 -> R.id.button3
        else -> R.id.button0
    }

    fun getImageViewId(i: Int): Int = when (i) {
        0 -> R.id.image0
        1 -> R.id.image1
        2 -> R.id.image2
        3 -> R.id.image3
        else -> R.id.image0
    }

    fun getImageId(i: Int): Int = when (i) {
        0 -> R.drawable.img0
        1 -> R.drawable.img1
        2 -> R.drawable.img2
        3 -> R.drawable.img3
        else -> R.drawable.img0
    }

    fun getTextId(i: Int): Int = when (i) {
        0 -> R.id.name0
        1 -> R.id.name1
        2 -> R.id.name2
        3 -> R.id.name3
        else -> R.id.name0
    }
}
