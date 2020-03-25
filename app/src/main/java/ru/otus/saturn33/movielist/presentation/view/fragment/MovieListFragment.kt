package ru.otus.saturn33.movielist.presentation.view.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
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
import ru.otus.saturn33.movielist.App
import ru.otus.saturn33.movielist.R
import ru.otus.saturn33.movielist.data.entity.MovieDTO
import ru.otus.saturn33.movielist.presentation.adapter.MovieListAdapter
import ru.otus.saturn33.movielist.presentation.decoration.CustomDecoration
import ru.otus.saturn33.movielist.presentation.interfaces.ActionBarProvider
import ru.otus.saturn33.movielist.presentation.viewmodel.MovieListViewModel
import ru.otus.saturn33.movielist.presentation.viewmodel.MovieListViewModelFactory
import java.util.*

class MovieListFragment : Fragment() {

    private var listener: OnDetailedClickListener? = null
    private var actionBarProvider: ActionBarProvider? = null

    private var viewModel: MovieListViewModel? = null
    private var moviesAdapter: MovieListAdapter? = null
    private lateinit var recyclerView: RecyclerView

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
            ViewModelProvider(it, MovieListViewModelFactory(App.instance!!, null)).get(
                MovieListViewModel::class.java
            )
        }
        viewModel?.movies?.observe(this.viewLifecycleOwner, Observer<List<MovieDTO>> {
            moviesAdapter?.setItems(it)
            viewModel?.isFirstAdd?.value?.let { isFirst ->
                if (isFirst) {
                    recyclerView.layoutManager?.scrollToPosition(0)
                    viewModel?.onFirstPageScrolled()
                }
            }

        })
        viewModel?.error?.observe(this.viewLifecycleOwner, Observer {
            handleError(view, it)
        })

        viewModel?.toast?.observe(this.viewLifecycleOwner, Observer {
            handleToast(view, it)
        })

        initRecycler(view)
        initSwipeRefresh(view)

        if (viewModel?.getCurrentPage() ?: 0 < 1) {
            viewModel?.onNextPageRequest()
        }
    }

    private fun handleError(view: View, error: String?) {
        if (error == null) {
            return
        } else {
            Snackbar.make(
                view,
                error,
                Snackbar.LENGTH_LONG
            ).setAction(getString(R.string.retry)) {
                viewModel?.onNextPageRequest()
            }.show()
            viewModel?.onErrorHandled()
        }
    }

    private fun handleToast(view: View, message: String?) {
        if (message == null) {
            return
        } else {
            Toast.makeText(view.context, message, Toast.LENGTH_LONG).show()
            viewModel?.onToastHandled()
        }
    }

    private fun initSwipeRefresh(view: View) {
        val swipeRefresher = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefresher)
        swipeRefresher.setOnRefreshListener {
            moviesAdapter?.clearItems()
            viewModel?.onRefresh()
            viewModel?.onNextPageRequest()
        }
        viewModel?.swipeRefresher?.observe(this.viewLifecycleOwner, Observer<Boolean> {
            swipeRefresher.isRefreshing = it
        })
    }

    private fun initRecycler(view: View) {
        recyclerView = view.findViewById(R.id.recyclerView)
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
                    DatePickerDialog(view.context).apply {
                        item.postponeMillis?.let {
                            val cal = Calendar.getInstance()
                            cal.timeInMillis = it
                            updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
                        }
                        setOnDateSetListener { _, year, month, dayOfMonth ->
                            val cal = Calendar.getInstance()
                            cal.set(year, month, dayOfMonth, 10, 0, 0)
                            viewModel?.onMoviePostpone(item, cal.time)
                        }
                    }.show()
                }
            }

        recyclerView.adapter = moviesAdapter

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
                viewModel?.setLastSeenPosition(layoutManager.findFirstVisibleItemPosition())
                moviesAdapter?.let {
                    if (layoutManager.findLastVisibleItemPosition() >= it.itemCount - 1 - LOAD_NEXT_PAGE_BEFORE_LAST_ELEMENTS && it.itemCount > 0) {
                        viewModel?.onNextPageRequest()
                    }
                }
            }
        })

        viewModel?.lastSeenPosition?.value?.let {
            layoutManager.scrollToPosition(it)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (activity is OnDetailedClickListener) {
            listener = activity as OnDetailedClickListener
        }
    }

    companion object {
        const val TAG = "MovieList"
        const val LOAD_NEXT_PAGE_BEFORE_LAST_ELEMENTS = 5
    }

    interface OnDetailedClickListener {
        fun onDetailedClick(item: MovieDTO)
    }
}
