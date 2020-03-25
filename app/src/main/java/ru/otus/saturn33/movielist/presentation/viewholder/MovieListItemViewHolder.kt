package ru.otus.saturn33.movielist.presentation.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.otus.saturn33.movielist.R
import ru.otus.saturn33.movielist.data.entity.MovieDTO

class MovieListItemViewHolder(
    itemView: View,
    private val tapListener: ((MovieDTO, position: Int) -> Unit)?,
    private val favListener: ((MovieDTO, position: Int) -> Unit)?,
    private val postponeListener: ((MovieDTO, position: Int) -> Unit)?
) :
    RecyclerView.ViewHolder(itemView) {
    private val imgIv: ImageView = itemView.findViewById(R.id.imageIv)
    private val ratingTv: TextView = itemView.findViewById(R.id.ratingTv)
    private val titleTv: TextView = itemView.findViewById(R.id.titleTv)
    private val inFavIv: ImageView = itemView.findViewById(R.id.inFavIv)
    private val postponeIv: ImageView = itemView.findViewById(R.id.postponeIv)

    fun bind(item: MovieDTO, colors: Pair<Int, Int>) {
        Glide.with(itemView)
            .load(item.imageURL)
            .centerCrop()
            .placeholder(R.drawable.movie_filler)
            .fallback(R.drawable.movie_filler)
            .error(R.drawable.movie_filler)
            .into(imgIv)

        titleTv.text = item.name
        ratingTv.text = item.rating.toString()
        titleTv.setTextColor(if (item.checked) colors.first else colors.second)
        inFavIv.setImageResource(if (item.inFav) R.drawable.favorite_yes else R.drawable.favorite_no)
        postponeIv.setImageResource(if (item.postponed) R.drawable.postpone_yes else R.drawable.postpone_no)

        itemView.setOnClickListener {
            tapListener?.invoke(item, adapterPosition)
        }

        inFavIv.setOnClickListener {
            favListener?.invoke(item, adapterPosition)
        }

        postponeIv.setOnClickListener {
            postponeListener?.invoke(item, adapterPosition)
        }
    }
}
