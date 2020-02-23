package ru.otus.saturn33.movielist.presentation.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.otus.saturn33.movielist.R
import ru.otus.saturn33.movielist.data.entity.MovieDTO

class FavoritesItemViewHolder(
    itemView: View,
    private val tapListener: ((MovieDTO, Int) -> Unit)?,
    private val longListener: ((MovieDTO, Int) -> Unit)?
) :
    RecyclerView.ViewHolder(itemView) {
    private val imgIv: ImageView = itemView.findViewById(R.id.imageIv)
    private val ratingTv: TextView = itemView.findViewById(R.id.ratingTv)
    private val titleTv: TextView = itemView.findViewById(R.id.titleTv)

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

        itemView.setOnClickListener {
            tapListener?.invoke(item, adapterPosition)
        }

        itemView.setOnLongClickListener {
            longListener?.invoke(item, adapterPosition)
            true
        }
    }
}
