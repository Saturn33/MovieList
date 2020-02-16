package ru.otus.saturn33.movielist.presentation.view.fragment

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.content.ContextCompat.getDrawable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import ru.otus.saturn33.movielist.R
import ru.otus.saturn33.movielist.data.entity.MovieDTO
import ru.otus.saturn33.movielist.presentation.`interface`.ActionBarProvider
import ru.otus.saturn33.movielist.presentation.adapter.MovieListAdapter
import ru.otus.saturn33.movielist.presentation.decoration.CustomDecoration
import ru.otus.saturn33.movielist.presentation.viewmodel.MovieListViewModel

class MovieListFragment : Fragment() {

    private var inUpdate = false
    private var listener: OnClickListener? = null
    private var adapterProvider: AdapterProvider? = null
    private var actionBarProvider: ActionBarProvider? = null

    private var viewModel: MovieListViewModel? = null
    private var moviesAdapter: MovieListAdapter? = null

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

        viewModel = activity?.let {
            ViewModelProvider(it).get(MovieListViewModel::class.java)
        }
        viewModel?.movies?.observe(this.viewLifecycleOwner, Observer<List<MovieDTO>> {
            moviesAdapter?.setItems(it)
        })

        initRecycler(view)
        initSwipeRefresh(view)

        viewModel?.onNextPageRequest()
    }

    private fun initSwipeRefresh(view: View) {
        val swipeRefresher = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefresher)
        swipeRefresher.setOnRefreshListener {
            moviesAdapter?.clearItems()
            viewModel?.onRefresh()
            viewModel?.onNextPageRequest()
        }
        viewModel?.swipeRefreshener?.observe(this.viewLifecycleOwner, Observer<Boolean> {
            swipeRefresher.isRefreshing = it
        })
    }

    private fun initRecycler(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val layoutManager = when (resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> GridLayoutManager(context, 2).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when (recyclerView.adapter?.getItemViewType(position)) {
                            MovieListAdapter.VIEW_TYPE_ITEM -> 1
                            MovieListAdapter.VIEW_TYPE_FOOTER -> 2
                            else -> -1
                        }
                    }
                }
            }
            else -> LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        val colorPair = Pair(
            resources.getColor(R.color.colorAccent, activity?.theme),
            resources.getColor(R.color.colorPrimary, activity?.theme)
        )
        recyclerView.layoutManager = layoutManager
        moviesAdapter =
            MovieListAdapter(LayoutInflater.from(context), colorPair).apply {
                tapListener = { item, position ->
                    listener?.onDetailedClick(item)
                }
                favListener = { item, position ->
                    viewModel?.onMovieLike(item)
                    Snackbar.make(
                        recyclerView,
                        if (!item.inFav) R.string.favorites_added else R.string.favorites_removed,
                        Snackbar.LENGTH_LONG
                    ).setAction(context?.getString(R.string.cancel)) {
                        viewModel?.onMovieLike(item)
                    }.show()
                }
            }

        recyclerView.adapter = moviesAdapter
        adapterProvider?.onAdapterCreated(moviesAdapter)

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
                viewModel?.lastSeenPosition = layoutManager.findFirstVisibleItemPosition()
                moviesAdapter?.let {
                    if (layoutManager.findLastVisibleItemPosition() >= it.itemCount - 1 - LOAD_NEXT_PAGE_BEFORE_LAST_ELEMENTS && it.itemCount > 0) {
                        viewModel?.onNextPageRequest()
                    }
                }
            }
        })

        viewModel?.lastSeenPosition?.let {
            if (it > 0) {
                layoutManager.scrollToPosition(it)
            }
        }
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
        const val LOAD_NEXT_PAGE_BEFORE_LAST_ELEMENTS = 5
    }

    interface OnClickListener {
        fun onDetailedClick(item: MovieDTO)
    }

    interface AdapterProvider {
        fun onAdapterCreated(adapter: MovieListAdapter?)
    }
}
