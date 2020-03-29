package ru.otus.saturn33.movielist.data.entity

import androidx.room.*

@Dao
interface PostponeDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(postpone: PostponeDTO)

    @Delete
    fun delete(postpone: PostponeDTO)

    @Query("DELETE FROM postpone")
    fun clear()

    @Query("SELECT * FROM postpone WHERE movie_id = :id")
    fun read(id: Int): PostponeDTO?

    @Query("SELECT * FROM postpone")
    fun getAll(): List<PostponeDTO>
}
