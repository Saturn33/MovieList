package app.whiletrue.movielist.data.entity

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface MovieDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(movie: MovieDTO): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(movies: List<MovieDTO>): Single<List<Long>?>

    @Update
    fun update(movie: MovieDTO): Completable

    @Delete
    fun delete(movie: MovieDTO): Completable

    @Delete
    fun delete(movies: List<MovieDTO>): Completable

    @Query("DELETE FROM movies WHERE id = :id")
    fun delete(id: Int): Completable

    @Query("DELETE FROM movies")
    fun clear(): Completable

    @Query("SELECT * FROM movies WHERE id = :id")
    fun read(id: Int): Single<MovieDTO?>?

    @Query("SELECT * FROM movies WHERE id IN (:ids)")
    fun read(ids: List<Int>): Single<List<MovieDTO>>

    @Query("SELECT * FROM movies ORDER BY rating DESC LIMIT 20 OFFSET (:page-1) * 20 ")
    fun browse(page: Int): Single<List<MovieDTO>>

    @Query("SELECT * FROM movies ORDER BY rating DESC")
    fun getAll(): Single<List<MovieDTO>>

}
