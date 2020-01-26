package ru.otus.saturn33.movielist.ui

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.otus.saturn33.movielist.R
import ru.otus.saturn33.movielist.data.MovieDTO
import ru.otus.saturn33.movielist.data.Storage
import ru.otus.saturn33.movielist.ui.adapters.MovieListAdapter
import ru.otus.saturn33.movielist.ui.dialogs.ExitDialog

class ListActivity : AppCompatActivity() {
    private var themeMode = AppCompatDelegate.MODE_NIGHT_NO

    private fun setThemeCycle() {
        themeMode =
            if (themeMode == AppCompatDelegate.MODE_NIGHT_NO) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(themeMode)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(THEME_KEY, themeMode)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            AppCompatDelegate.setDefaultNightMode(themeMode)
        } else {
            themeMode = savedInstanceState.getInt(THEME_KEY, AppCompatDelegate.MODE_NIGHT_NO)
        }

        setContentView(R.layout.activity_list)
        initRecycler()
        initSwipeRefresh()

        findViewById<TextView>(R.id.invite).setOnClickListener {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getString(R.string.try_app))
            }
            sendIntent.resolveActivity(packageManager)?.let {
                startActivity(sendIntent)
            }
        }
        findViewById<TextView>(R.id.new_movie).setOnClickListener {
            startActivityForResult(
                Intent(this, NewMovieActivity::class.java),
                REQUEST_CODE_NEW_MOVIE
            )
        }
    }

    private fun initSwipeRefresh() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val swipeRefresher = findViewById<SwipeRefreshLayout>(R.id.swipeRefresher)
        swipeRefresher.setOnRefreshListener {
            recyclerView.adapter?.notifyItemRangeRemoved(0, Storage.movies.size)
            Storage.movies.clear()
            Storage.movies.addAll(Storage.moviesInitial)
            recyclerView.adapter?.notifyItemRangeInserted(0, Storage.movies.size)
            swipeRefresher.isRefreshing = false
        }
    }

    private fun initRecycler() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val layoutManager = when (resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> GridLayoutManager(this, 2)
            else -> LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        }
        val colorPair = Pair(
            resources.getColor(R.color.colorAccent, theme),
            resources.getColor(R.color.colorPrimary, theme)
        )
        Storage.movies.clear()
        Storage.movies.addAll(Storage.moviesInitial)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter =
            MovieListAdapter(LayoutInflater.from(this), Storage.movies, colorPair)

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_theme -> {
                setThemeCycle()
                true
            }
            R.id.action_favorites -> {
                startActivity(
                    Intent(this, FavoritesActivity::class.java)
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_NEW_MOVIE && resultCode == Activity.RESULT_OK) {
            data?.let {
                val movie: MovieDTO? = it.getParcelableExtra(MOVIE_KEY)
                movie?.let { movieDTO ->
                    Storage.movies.add(movieDTO)
                    val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
                    recyclerView.adapter?.notifyItemInserted(Storage.movies.size)
                }
            }
        }
    }

    override fun onBackPressed() {
        val dialog = ExitDialog(this)
        dialog.setOnCancelListener {
            super.onBackPressed()
        }
        dialog.show()
    }

    companion object {
        const val TAG = "TST"
        const val THEME_KEY = "theme"
        const val REQUEST_CODE_NEW_MOVIE = 0
        const val MOVIE_KEY = "movie"
    }
}
