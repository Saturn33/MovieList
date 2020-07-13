package app.whiletrue.movielist.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import app.whiletrue.movielist.data.entity.MovieDTO

class MovieListDiff(private val oldList: List<MovieDTO>, private val newList: List<MovieDTO>) :
    DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].id == newList[newItemPosition].id
}