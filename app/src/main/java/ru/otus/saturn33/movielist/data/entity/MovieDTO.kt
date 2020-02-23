package ru.otus.saturn33.movielist.data.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieDTO(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val name: String,
    @SerializedName("overview")
    val description: String,
    @SerializedName("vote_average")
    val rating: Double,
    @SerializedName("poster_path")
    val imagePath: String? = null,

    var inFav: Boolean = false,
    var checked: Boolean = false,
    var imageURL: String? = null
) : Parcelable
