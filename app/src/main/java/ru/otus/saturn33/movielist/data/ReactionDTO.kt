package ru.otus.saturn33.movielist.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReactionDTO(
    var liked: Boolean = false,
    var comment: String = ""
) : Parcelable