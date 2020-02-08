package ru.otus.saturn33.movielist.ui.fragments

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat.getDrawable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.otus.saturn33.movielist.R
import ru.otus.saturn33.movielist.data.MovieDTO
import ru.otus.saturn33.movielist.data.Storage
import ru.otus.saturn33.movielist.ui.adapters.MovieListAdapter
import ru.otus.saturn33.movielist.ui.decorations.CustomDecoration
import ru.otus.saturn33.movielist.ui.interfaces.ActionBarProvider

class MovieListFragment : Fragment() {

    private var listener: OnClickListener? = null
    private var adapterProvider: AdapterProvider? = null
    private var actionBarProvider: ActionBarProvider? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        if (activity is ActionBarProvider) {
            actionBarProvider = activity as ActionBarProvider
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actionBarProvider?.changeTitle(getString(R.string.movie_list))

        initRecycler(view)
        initSwipeRefresh(view)

        view.findViewById<TextView>(R.id.invite).setOnClickListener {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getString(R.string.try_app))
            }
            sendIntent.resolveActivity(activity!!.packageManager)?.let {
                startActivity(sendIntent)
            }
        }
        view.findViewById<TextView>(R.id.new_movie).setOnClickListener {
            listener?.onNewClick()
        }
    }

    private fun initSwipeRefresh(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val swipeRefresher = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefresher)
        swipeRefresher.setOnRefreshListener {
            recyclerView.adapter?.notifyItemRangeRemoved(0, Storage.movies.size)
            Storage.movies.clear()
            Storage.movies.addAll(Storage.moviesInitial)
            recyclerView.adapter?.notifyItemRangeInserted(0, Storage.movies.size)
            swipeRefresher.isRefreshing = false
        }
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
        val newAdapter = MovieListAdapter(LayoutInflater.from(context), Storage.movies, colorPair) {
            listener?.onDetailedClick(it)
        }
        recyclerView.adapter = newAdapter
        adapterProvider?.onAdapterCreated(newAdapter)

        getDrawable(context!!, R.drawable.custom_line)?.let {
            recyclerView.addItemDecoration(
                CustomDecoration(
                    context as Context,
                    DividerItemDecoration.VERTICAL,
                    it,
                    100,
                    100
                )
            )
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (layoutManager.findLastCompletelyVisibleItemPosition() == Storage.movies.size) {
                    Storage.movies.addAll(Storage.additionalMovies)
                    recyclerView.adapter?.notifyItemRangeInserted(
                        Storage.movies.size - Storage.additionalMovies.size,
                        Storage.additionalMovies.size
                    )
                }
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (activity is OnClickListener) {
            listener = activity as OnClickListener
        }
        if (activity is AdapterProvider) {
            adapterProvider = activity as AdapterProvider
        }
    }

    companion object {
        const val TAG = "MovieList"
    }

    interface OnClickListener {
        fun onDetailedClick(item: MovieDTO)
        fun onNewClick()
    }

    interface AdapterProvider {
        fun onAdapterCreated(adapter: MovieListAdapter)
    }
}
