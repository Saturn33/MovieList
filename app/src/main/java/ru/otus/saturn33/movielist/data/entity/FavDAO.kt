package ru.otus.saturn33.movielist.data.entity

import androidx.room.*

@Dao
interface FavDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(fav: FavDTO)

    @Delete
    fun delete(fav: FavDTO)

    @Query("DELETE FROM favorites")
    fun clear()

    @Query("SELECT * FROM favorites WHERE movie_id = :id")
    fun read(id: Int): FavDTO?

    @Query("SELECT * FROM favorites")
    fun getAll(): List<FavDTO>
}
