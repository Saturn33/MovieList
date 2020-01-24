package ru.otus.saturn33.movielist.ui.viewholders

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.otus.saturn33.movielist.R
import ru.otus.saturn33.movielist.data.MovieDTO
import ru.otus.saturn33.movielist.ui.DetailActivity
import ru.otus.saturn33.movielist.ui.adapters.MovieListAdapter

class MovieListViewHolder(itemView: View, private val adapter: MovieListAdapter) :
    RecyclerView.ViewHolder(itemView) {
    private val imgIv: ImageView = itemView.findViewById(R.id.imageIv)
    private val titleTv: TextView = itemView.findViewById(R.id.titleTv)
    private val inFavIv: ImageView = itemView.findViewById(R.id.inFavIv)

    fun bind(item: MovieDTO, colors: Pair<Int, Int>) {
        imgIv.setImageResource(item.imageId ?: R.drawable.movie_filler)
        titleTv.text = item.name
        titleTv.setTextColor(if (item.checked) colors.first else colors.second)
        inFavIv.setImageResource(if (item.inFav) R.drawable.favorite_yes else R.drawable.favorite_no)

        itemView.setOnClickListener {
            item.checked = true
            itemView.context.startActivity(
                Intent(itemView.context, DetailActivity::class.java).apply {
                    putExtra("movie", item)
                }
            )
            adapter.notifyItemChanged(adapterPosition)
        }

        inFavIv.setOnClickListener {
            item.inFav = !item.inFav
            adapter.notifyItemChanged(adapterPosition)
        }
    }
}