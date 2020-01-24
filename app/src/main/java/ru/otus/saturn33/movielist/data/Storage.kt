package ru.otus.saturn33.movielist.data

import ru.otus.saturn33.movielist.R

object Storage {
    val movies: MutableList<MovieDTO> = arrayListOf()
    val moviesInitial: List<MovieDTO> = arrayListOf(
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
    val additionalMovies: List<MovieDTO> = arrayListOf(
        MovieDTO(
            "Бойцовский клуб",
            "An insomniac office worker and a devil-may-care soapmaker form an underground fight club that evolves into something much, much more.",
            R.drawable.fightclub
        ),
        MovieDTO(
            "Молчание ягнят",
            "A young F.B.I. cadet must receive the help of an incarcerated and manipulative cannibal killer to help catch another serial killer, a madman who skins his victims.",
            R.drawable.silence
        ),
        MovieDTO(
            "Паразиты",
            "All unemployed, Ki-taek and his family take peculiar interest in the wealthy and glamorous Parks, as they ingratiate themselves into their lives and get entangled in an unexpected incident.",
            R.drawable.parasite
        ),
        MovieDTO(
            "Спасти рядового Райана",
            "Following the Normandy Landings, a group of U.S. soldiers go behind enemy lines to retrieve a paratrooper whose brothers have been killed in action.",
            R.drawable.saverayan
        ),
        MovieDTO(
            "Зелёная миля",
            "The lives of guards on Death Row are affected by one of their charges: a black man accused of child murder and rape, yet who has a mysterious gift.\n",
            R.drawable.green
        ),
        MovieDTO(
            "Интерстеллар",
            "A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.",
            R.drawable.interstellar
        )
    )

}
