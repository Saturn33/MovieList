package app.whiletrue.movielist.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.whiletrue.movielist.R
import app.whiletrue.movielist.data.entity.MovieDTO
import app.whiletrue.movielist.presentation.viewholder.FavoritesItemViewHolder

class FavoritesListAdapter(
    private val inflater: LayoutInflater,
    private val colors: Pair<Int, Int>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var tapListener: ((MovieDTO, Int) -> Unit)? = null
    var longListener: ((MovieDTO, Int) -> Unit)? = null
    val items : MutableList<MovieDTO> = mutableListOf()

    fun setItems(movies: List<MovieDTO>) {
        val diffResult = DiffUtil.calculateDiff(MovieListDiff(items, movies))
        items.clear()
        items.addAll(movies.map { it.copy() })

        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ITEM -> FavoritesItemViewHolder(
                inflater.inflate(
                    R.layout.item_favorites,
                    parent,
                    false
                ), tapListener, longListener
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
