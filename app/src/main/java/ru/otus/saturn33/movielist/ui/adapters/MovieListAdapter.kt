package ru.otus.saturn33.movielist.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.otus.saturn33.movielist.R
import ru.otus.saturn33.movielist.data.MovieDTO
import ru.otus.saturn33.movielist.ui.viewholders.MovieListViewHolder

class MovieListAdapter(
    private val inflater: LayoutInflater,
    private val items: List<MovieDTO>,
    private val colors: Pair<Int, Int>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MovieListViewHolder(inflater.inflate(R.layout.item_movie_list, parent, false), this)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MovieListViewHolder -> holder.bind(items[position], colors)
        }
    }
}