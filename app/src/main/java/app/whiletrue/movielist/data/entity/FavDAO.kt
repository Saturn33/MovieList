package app.whiletrue.movielist.data.entity

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface FavDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(fav: FavDTO): Completable

    @Delete
    fun delete(fav: FavDTO): Completable

    @Query("DELETE FROM favorites")
    fun clear(): Completable

    @Query("SELECT * FROM favorites WHERE movie_id = :id")
    fun read(id: Int): Single<FavDTO?>

    @Query("SELECT * FROM favorites")
    fun getAll(): Single<List<FavDTO>>
}
