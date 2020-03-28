package ru.otus.saturn33.movielist.presentation.view.fragment

import android.app.DatePickerDialog
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.otus.saturn33.movielist.R
import ru.otus.saturn33.movielist.data.entity.MovieDTO
import ru.otus.saturn33.movielist.presentation.adapter.MovieListAdapter
import ru.otus.saturn33.movielist.presentation.adapter.PostponedListAdapter
import ru.otus.saturn33.movielist.presentation.dialog.PostponeHelper
import ru.otus.saturn33.movielist.presentation.interfaces.ActionBarProvider
import ru.otus.saturn33.movielist.presentation.viewmodel.MovieListViewModel
import java.util.*

class MoviePostponedFragment : Fragment() {

    private var listener: OnDetailedClickListener? = null
    private var actionBarProvider: ActionBarProvider? = null
    private var viewModel: MovieListViewModel? = null
    private var moviesAdapter: MovieListAdapter? = null


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
        actionBarProvider?.changeTitle(getString(R.string.title_postponed))

        viewModel = activity?.let {
            ViewModelProvider(it).get(MovieListViewModel::class.java)
        }
        viewModel?.moviesPostponed?.observe(this.viewLifecycleOwner, Observer<List<MovieDTO>> {
            moviesAdapter?.setItems(it)
        })

        initRecycler(view)
    }

    private fun initRecycler(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val layoutManager = when (resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> GridLayoutManager(context, 2)
            else -> LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        val colorPair = Pair(
            resources.getColor(R.color.colorAccent, activity?.theme),
            resources.getColor(R.color.colorPrimary, activity?.theme)
        )
        recyclerView.layoutManager = layoutManager
        moviesAdapter =
            PostponedListAdapter(LayoutInflater.from(context), colorPair).apply {
                tapListener = { item, _ ->
                    listener?.onDetailedClick(item)
                }
                favListener = { item, _ ->
                    viewModel?.onMovieLike(item)
                    Snackbar.make(
                        recyclerView,
                        if (!item.inFav) R.string.favorites_added else R.string.favorites_removed,
                        Snackbar.LENGTH_LONG
                    ).setAction(context?.getString(R.string.cancel)) {
                        viewModel?.onMovieLike(item)
                    }.show()
                }
                postponeListener = { item, _ ->
                    PostponeHelper.selectDate(view, item, viewModel)
                }
            }

        recyclerView.adapter = moviesAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (activity is OnDetailedClickListener) {
            listener = activity as OnDetailedClickListener
        }
    }

    companion object {
        const val TAG = "MoviePostponed"
    }

    interface OnDetailedClickListener {
        fun onDetailedClick(item: MovieDTO)
    }
}
