package ru.otus.saturn33.movielist.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import ru.otus.saturn33.movielist.R
import ru.otus.saturn33.movielist.data.MovieDTO
import ru.otus.saturn33.movielist.data.Storage
import ru.otus.saturn33.movielist.ui.adapters.MovieListAdapter
import ru.otus.saturn33.movielist.ui.dialogs.ExitDialog
import ru.otus.saturn33.movielist.ui.fragments.MovieDetailFragment
import ru.otus.saturn33.movielist.ui.fragments.MovieFavoritesFragment
import ru.otus.saturn33.movielist.ui.fragments.MovieListFragment
import ru.otus.saturn33.movielist.ui.fragments.NewMovieFragment

class MainActivity : AppCompatActivity(), MovieListFragment.OnClickListener,
    MovieListFragment.AdapterProvider,
    MovieFavoritesFragment.OnDetailedClickListener,
    NavigationView.OnNavigationItemSelectedListener,
    NewMovieFragment.OnNewMovieClickListener {
    private var themeMode = AppCompatDelegate.MODE_NIGHT_NO
    private var movieListAdapter: MovieListAdapter? = null

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

        setContentView(R.layout.activity_main)

        val activityToolbar: Toolbar? = findViewById(R.id.toolbar)
        setSupportActionBar(activityToolbar)

        supportFragmentManager.addOnBackStackChangedListener {
            updateToolBar(activityToolbar)
        }

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        Storage.movies.clear()
        Storage.movies.addAll(Storage.moviesInitial)
        openList(false)
        updateToolBar(activityToolbar)
    }

    private fun updateToolBar(activityToolbar: Toolbar?) {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.fragments.last {
                when (it.tag) {
                    MovieDetailFragment.TAG -> {
                        activityToolbar?.visibility = View.GONE
                        val fragmentToolbar = it.view?.findViewById<Toolbar>(R.id.toolbarAdvanced)
                        setSupportActionBar(fragmentToolbar)
                    }
                    else -> {
                        activityToolbar?.visibility = View.VISIBLE
                        setSupportActionBar(activityToolbar)
                    }
                }
                when (it.tag) {
                    MovieListFragment.TAG -> activityToolbar?.title = getString(R.string.movie_list)
                    NewMovieFragment.TAG -> activityToolbar?.title =
                        getString(R.string.title_new_movie)
                    MovieFavoritesFragment.TAG -> activityToolbar?.title =
                        getString(R.string.title_favorites)
                }
                true
            }
        } else {
            activityToolbar?.visibility = View.VISIBLE
            setSupportActionBar(activityToolbar)
            activityToolbar?.title = getString(R.string.movie_list)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(supportFragmentManager.backStackEntryCount > 0)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                supportFragmentManager.popBackStack()
                true
            }
            R.id.action_theme -> {
                setThemeCycle()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            val dialog = ExitDialog(this) {
                if (it) super.onBackPressed()
            }
            dialog.show()
        }
    }


    companion object {
        const val THEME_KEY = "theme"
        const val FRAGMENT_LIST = "FragmentList"
        const val FRAGMENT_DETAILS = "FragmentDetails"
        const val FRAGMENT_FAVORITE = "FragmentFavorite"
        const val FRAGMENT_NEW = "FragmentNew"
    }

    override fun onDetailedClick(item: MovieDTO) {
        openDetailed(item)
    }

    override fun onNewClick() {
        openNewMovie()
    }

    private fun openDetailed(item: MovieDTO) {
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragmentContainer,
                MovieDetailFragment.newInstance(item),
                MovieDetailFragment.TAG
            )
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(FRAGMENT_DETAILS)
            .commit()
    }

    private fun openFavorites() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, MovieFavoritesFragment(), MovieFavoritesFragment.TAG)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(FRAGMENT_FAVORITE)
            .commit()
    }

    private fun openList(addToBackStack: Boolean = true) {
        val ft = supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, MovieListFragment(), MovieListFragment.TAG)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        if (addToBackStack)
            ft.addToBackStack(FRAGMENT_LIST)
        ft.commit()
    }

    private fun openNewMovie() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, NewMovieFragment(), NewMovieFragment.TAG)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(FRAGMENT_NEW)
            .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_list -> openList()
            R.id.nav_favorite -> openFavorites()
            R.id.nav_new -> openNewMovie()
        }
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)

        return true
    }

    override fun onNewMovieClick(item: MovieDTO) {
        Storage.movies.add(item)
        movieListAdapter?.notifyItemInserted(Storage.movies.size)
    }

    override fun onAdapterCreated(adapter: MovieListAdapter) {
        movieListAdapter = adapter
    }
}
