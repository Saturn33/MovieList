package ru.otus.saturn33.movielist.data.entity

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface PostponeDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(postpone: PostponeDTO): Completable

    @Delete
    fun delete(postpone: PostponeDTO): Completable

    @Query("DELETE FROM postpone")
    fun clear(): Completable

    @Query("SELECT * FROM postpone WHERE movie_id = :id")
    fun read(id: Int): Single<PostponeDTO?>

    @Query("SELECT * FROM postpone")
    fun getAll(): Single<List<PostponeDTO>>
}
