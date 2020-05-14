package app.whiletrue.movielist.presentation.adapter

import android.view.LayoutInflater

class PostponedListAdapter(inflater: LayoutInflater, colors: Pair<Int, Int>) : MovieListAdapter(
    inflater,
    colors
) {
    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = VIEW_TYPE_ITEM
}
