package ru.otus.saturn33.movielist.ui.fragments

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.core.content.ContextCompat.getDrawable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.otus.saturn33.movielist.R
import ru.otus.saturn33.movielist.data.MovieDTO
import ru.otus.saturn33.movielist.data.MoviesResponse
import ru.otus.saturn33.movielist.data.Storage
import ru.otus.saturn33.movielist.network.ApiClient
import ru.otus.saturn33.movielist.network.BaseApi.Companion.LOAD_NEXT_PAGE_BEFORE_LAST_ELEMENTS
import ru.otus.saturn33.movielist.ui.adapters.MovieListAdapter
import ru.otus.saturn33.movielist.ui.decorations.CustomDecoration
import ru.otus.saturn33.movielist.ui.interfaces.ActionBarProvider

class MovieListFragment : Fragment() {

    private var inUpdate = false
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
        initRecycler(view)
        initSwipeRefresh(view)
        if (Storage.movies.size == 0)
            loadNextPage {
                if (it.isEmpty())
                    showLoadError(view)
                Storage.movies.addAll(it)
                val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
                recyclerView.adapter?.notifyItemRangeInserted(
                    Storage.movies.size - it.size,
                    it.size
                )
                recyclerView.scrollToPosition(0)
                inUpdate = false
            }
    }

    private fun initSwipeRefresh(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val swipeRefresher = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefresher)
        swipeRefresher.setOnRefreshListener {
            recyclerView.adapter?.notifyItemRangeRemoved(0, Storage.movies.size)
            val size = Storage.movies.size
            Storage.movies.clear()
            recyclerView.adapter?.notifyItemRangeRemoved(0, size)
            Storage.page = 0
            loadNextPage {
                if (it.isEmpty())
                    showLoadError(view)
                Storage.movies.addAll(it)
                recyclerView.adapter?.notifyItemRangeInserted(0, Storage.movies.size)
                recyclerView.scrollToPosition(0)
                swipeRefresher.isRefreshing = false
                inUpdate = false
            }
        }
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
        val newAdapter =
            MovieListAdapter(LayoutInflater.from(context), Storage.movies, colorPair).apply {
                tapListener = { item, position ->
                    Storage.checkedMovies.add(item.id)
                    this.notifyItemChanged(position)
                    listener?.onDetailedClick(item)
                }
                favListener = { item, position ->
                    if (Storage.favMovies.contains(item.id))
                        Storage.favMovies.remove(item.id)
                    else
                        Storage.favMovies.add(item.id)
                    notifyItemChanged(position)
                    Snackbar.make(
                        recyclerView,
                        if (Storage.favMovies.contains(item.id)) R.string.favorites_added else R.string.favorites_removed,
                        Snackbar.LENGTH_LONG
                    ).setAction(context?.getString(R.string.cancel)) {
                        if (Storage.favMovies.contains(item.id))
                            Storage.favMovies.remove(item.id)
                        else
                            Storage.favMovies.add(item.id)
                        this.notifyItemChanged(position)
                    }.show()
                }
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
                Storage.lastSeenPosition = layoutManager.findFirstVisibleItemPosition()
                if (layoutManager.findLastVisibleItemPosition() >= Storage.movies.size - LOAD_NEXT_PAGE_BEFORE_LAST_ELEMENTS && Storage.movies.size > 0) {
                    loadNextPage {
                        if (it.isEmpty())
                            showLoadError(view)
                        Storage.movies.addAll(it)
                        recyclerView.adapter?.notifyItemRangeInserted(
                            Storage.movies.size - it.size,
                            it.size
                        )
                        inUpdate = false
                    }
                }
            }
        })

        if (Storage.lastSeenPosition > 0)
            layoutManager.scrollToPosition(Storage.lastSeenPosition)
    }

    private fun showLoadError(view: View) {
        Snackbar.make(view, getString(R.string.error_loading), Snackbar.LENGTH_LONG).show()
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
    }

    interface AdapterProvider {
        fun onAdapterCreated(adapter: MovieListAdapter)
    }

    fun loadNextPage(callback: ((List<MovieDTO>) -> Unit)?) {
        if (inUpdate) return
        inUpdate = true
        val call: Call<MoviesResponse>? = ApiClient.service.getTopRatedMovies(Storage.page + 1)
        call?.enqueue(object : Callback<MoviesResponse> {
            override fun onFailure(call: Call<MoviesResponse>?, t: Throwable?) {
                Log.e("Main", t.toString())
                callback?.invoke(listOf())
            }

            override fun onResponse(
                call: Call<MoviesResponse>?,
                response: Response<MoviesResponse>?
            ) {
                val movies = response?.body()?.results ?: listOf()
                val receivedPage = response?.body()?.page ?: 0
                if (receivedPage > Storage.page)
                    Storage.page = receivedPage
                callback?.invoke(movies)
            }
        })
    }
}
