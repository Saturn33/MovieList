package ru.otus.saturn33.movielist.ui.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MovieListFooterViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    fun bind(isEnd: Boolean) {
        itemView.visibility = if (isEnd) View.GONE else View.VISIBLE
    }
}
