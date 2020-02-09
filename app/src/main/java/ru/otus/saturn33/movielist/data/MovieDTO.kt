package ru.otus.saturn33.movielist.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import ru.otus.saturn33.movielist.network.BaseApi

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
    val imagePath: String? = null
) : Parcelable {
    fun getPath(): String? = if (imagePath == null) null else "${BaseApi.BASE_IMAGE_URL}$imagePath"
    fun inFav(): Boolean = Storage.favMovies.contains(id)
    fun checked(): Boolean = Storage.checkedMovies.contains(id)
}
