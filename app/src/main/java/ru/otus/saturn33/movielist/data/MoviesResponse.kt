package ru.otus.saturn33.movielist.data

import com.google.gson.annotations.SerializedName


data class MoviesResponse(
    @SerializedName("page")
    private val page: Int = 0,
    @SerializedName("results")
    private val results: List<Movie?>? = null,
    @SerializedName("total_results")
    private val totalResults: Int = 0,
    @SerializedName("total_pages")
    private val totalPages: Int = 0
) {

}