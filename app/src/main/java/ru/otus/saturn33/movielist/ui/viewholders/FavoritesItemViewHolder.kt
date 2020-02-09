package ru.otus.saturn33.movielist.ui.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.otus.saturn33.movielist.R
import ru.otus.saturn33.movielist.data.MovieDTO

class FavoritesItemViewHolder(
    itemView: View,
    private val tapListener: ((MovieDTO, Int) -> Unit)?,
    private val longListener: ((MovieDTO, Int) -> Unit)?
) :
    RecyclerView.ViewHolder(itemView) {
    private val imgIv: ImageView = itemView.findViewById(R.id.imageIv)
    private val titleTv: TextView = itemView.findViewById(R.id.titleTv)

    fun bind(item: MovieDTO, colors: Pair<Int, Int>) {
        imgIv.setImageResource(item.imageId ?: R.drawable.movie_filler)
        titleTv.text = item.name
        titleTv.setTextColor(if (item.checked()) colors.first else colors.second)

        itemView.setOnClickListener {
            tapListener?.invoke(item, adapterPosition)
        }

        itemView.setOnLongClickListener {
            longListener?.invoke(item, adapterPosition)
            true
        }
    }
}
