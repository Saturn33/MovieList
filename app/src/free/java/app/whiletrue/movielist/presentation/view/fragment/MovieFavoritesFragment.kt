package app.whiletrue.movielist.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.whiletrue.movielist.R
import app.whiletrue.movielist.data.entity.MovieDTO
import app.whiletrue.movielist.presentation.interfaces.ActionBarProvider

class MovieFavoritesFragment : Fragment() {
    private var actionBarProvider: ActionBarProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        if (activity is ActionBarProvider) {
            actionBarProvider = activity as ActionBarProvider
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actionBarProvider?.changeTitle(getString(R.string.title_favorites))

    }

    companion object {
        const val TAG = "MovieFavorites"
    }

    interface OnDetailedClickListener {
        fun onDetailedClick(item: MovieDTO)
    }
}
