package app.whiletrue.movielist.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "movies")
@Parcelize
data class MovieDTO(
    @SerializedName("id")
    @PrimaryKey(autoGenerate = false)
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
    var postponed: Boolean = false,
    var postponeMillis: Long?,
    var checked: Boolean = false,
    var imageURL: String? = null
) : Parcelable
