package ru.otus.saturn33.movielist.data.entity

import androidx.room.*

@Dao
interface MovieDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(movie: MovieDTO)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(movies: List<MovieDTO>): List<Long>?

    @Update
    fun update(movie: MovieDTO)

    @Delete
    fun delete(movie: MovieDTO)

    @Delete
    fun delete(movies: List<MovieDTO>)

    @Query("DELETE FROM movies WHERE id = :id")
    fun delete(id: Int)

    @Query("DELETE FROM movies")
    fun clear()

    @Query("SELECT * FROM movies WHERE id = :id")
    fun read(id: Int): MovieDTO?

    @Query("SELECT * FROM movies WHERE id IN (:ids)")
    fun read(ids: List<Int>): List<MovieDTO>

    @Query("SELECT * FROM movies ORDER BY rating DESC LIMIT 20 OFFSET (:page-1) * 20 ")
    fun browse(page: Int): List<MovieDTO>

    @Query("SELECT * FROM movies ORDER BY rating DESC")
    fun getAll(): List<MovieDTO>

}
