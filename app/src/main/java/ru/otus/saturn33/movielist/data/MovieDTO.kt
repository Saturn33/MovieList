package ru.otus.saturn33.movielist.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieDTO(
    val name: String,
    val description: String,
    val imageId: Int,
    var checked: Boolean = false,
    var inFav: Boolean = false
) : Parcelable
