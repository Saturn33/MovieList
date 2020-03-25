package ru.otus.saturn33.movielist.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.otus.saturn33.movielist.R
import ru.otus.saturn33.movielist.data.entity.MovieDTO
import ru.otus.saturn33.movielist.presentation.viewholder.MovieListFooterViewHolder
import ru.otus.saturn33.movielist.presentation.viewholder.MovieListItemViewHolder

class MovieListAdapter(
    private val inflater: LayoutInflater,
    private val colors: Pair<Int, Int>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var tapListener: ((MovieDTO, position: Int) -> Unit)? = null
    var favListener: ((MovieDTO, position: Int) -> Unit)? = null
    var postponeListener: ((MovieDTO, position: Int) -> Unit)? = null
    val items : MutableList<MovieDTO> = mutableListOf()

    fun setItems(movies: List<MovieDTO>) {
        val diffResult = DiffUtil.calculateDiff(MovieListDiff(items, movies))
        items.clear()
        items.addAll(movies.map { it.copy() })

        diffResult.dispatchUpdatesTo(this)
    }

    fun clearItems() {
        val diffResult = DiffUtil.calculateDiff(MovieListDiff(items, listOf()))
        items.clear()
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ITEM -> MovieListItemViewHolder(
                inflater.inflate(
                    R.layout.item_movie_list,
                    parent,
                    false
                ), tapListener, favListener, postponeListener
            )
            VIEW_TYPE_FOOTER -> MovieListFooterViewHolder(
                inflater.inflate(
                    R.layout.footer_movie_list,
                    parent,
                    false
                )
            )
            else -> throw Exception("Bad viewtype")
        }
    }

    override fun getItemCount() = items.size + 1 // + footer

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MovieListItemViewHolder -> holder.bind(items[position], colors)
            is MovieListFooterViewHolder -> holder.bind(false)//TODO когда-нибудь сюда будет прилетать флаг, что новых элементов больше нет
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            items.size -> VIEW_TYPE_FOOTER
            else -> VIEW_TYPE_ITEM
        }
    }

    companion object {
        const val VIEW_TYPE_ITEM = 0
        const val VIEW_TYPE_FOOTER = 1
    }
}
