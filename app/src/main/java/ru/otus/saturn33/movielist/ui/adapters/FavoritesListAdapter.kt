package ru.otus.saturn33.movielist.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.otus.saturn33.movielist.R
import ru.otus.saturn33.movielist.data.MovieDTO
import ru.otus.saturn33.movielist.ui.viewholders.FavoritesItemViewHolder

class FavoritesListAdapter(
    private val inflater: LayoutInflater,
    val items: MutableList<MovieDTO>,
    private val colors: Pair<Int, Int>,
    private val tapListener: ((MovieDTO) -> Unit)?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ITEM -> FavoritesItemViewHolder(
                inflater.inflate(
                    R.layout.item_favorites,
                    parent,
                    false
                ), this, tapListener
            )
            else -> throw Exception("Bad viewtype")
        }
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FavoritesItemViewHolder -> holder.bind(items[position], colors)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE_ITEM
    }

    companion object {
        const val VIEW_TYPE_ITEM = 0
    }
}
