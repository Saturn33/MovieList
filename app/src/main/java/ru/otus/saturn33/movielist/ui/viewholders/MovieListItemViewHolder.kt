package ru.otus.saturn33.movielist.ui.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.otus.saturn33.movielist.R
import ru.otus.saturn33.movielist.data.MovieDTO

class MovieListItemViewHolder(
    itemView: View,
    private val tapListener: ((MovieDTO, position: Int) -> Unit)?,
    private val favListener: ((MovieDTO, position: Int) -> Unit)?
) :
    RecyclerView.ViewHolder(itemView) {
    private val imgIv: ImageView = itemView.findViewById(R.id.imageIv)
    private val titleTv: TextView = itemView.findViewById(R.id.titleTv)
    private val inFavIv: ImageView = itemView.findViewById(R.id.inFavIv)

    fun bind(item: MovieDTO, colors: Pair<Int, Int>) {
        Glide.with(itemView)
            .load(item.getPath())
            .centerCrop()
            .placeholder(R.drawable.movie_filler)
            .fallback(R.drawable.movie_filler)
            .error(R.drawable.movie_filler)
            .into(imgIv)

        titleTv.text = item.name
        titleTv.setTextColor(if (item.checked()) colors.first else colors.second)
        inFavIv.setImageResource(if (item.inFav()) R.drawable.favorite_yes else R.drawable.favorite_no)

        itemView.setOnClickListener {
            tapListener?.invoke(item, adapterPosition)
        }

        inFavIv.setOnClickListener {
            val pos = adapterPosition
            favListener?.invoke(item, pos)
        }
    }
}
