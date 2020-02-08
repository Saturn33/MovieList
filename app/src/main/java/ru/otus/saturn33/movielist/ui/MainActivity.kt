package ru.otus.saturn33.movielist.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
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
import ru.otus.saturn33.movielist.ui.interfaces.ActionBarProvider

class MainActivity : AppCompatActivity(), MovieListFragment.OnClickListener,
    MovieListFragment.AdapterProvider,
    MovieFavoritesFragment.OnDetailedClickListener,
    NavigationView.OnNavigationItemSelectedListener,
    NewMovieFragment.OnNewMovieClickListener, ActionBarProvider {
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

        val activityToolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(activityToolbar)

        supportFragmentManager.addOnBackStackChangedListener {
            updateToolBar(activityToolbar)
        }

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        Storage.movies.clear()
        Storage.movies.addAll(Storage.moviesInitial)
        openList()
        updateToolBar(activityToolbar)
        initDrawer(findViewById(R.id.toolbar))
    }

    private fun initDrawer(toolbar: Toolbar) {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        ActionBarDrawerToggle(
            this, drawer, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        ).apply {
            isDrawerIndicatorEnabled = true
            syncState()
        }
    }

    private fun disableDrawer(toolbar: Toolbar?) {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        ActionBarDrawerToggle(
            this, drawer, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        ).apply {
            setToolbarNavigationClickListener {
                supportFragmentManager.popBackStack()
            }
            isDrawerIndicatorEnabled = false
            syncState()
        }
    }

    private fun updateToolBar(activityToolbar: Toolbar) {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.fragments.last {
                when (it.tag) {
                    MovieDetailFragment.TAG, NewMovieFragment.TAG -> {
                        activityToolbar.visibility = View.GONE
                        val fragmentToolbar = it.view?.findViewById<Toolbar>(R.id.toolbarAdvanced)
                        setSupportActionBar(fragmentToolbar)
                        disableDrawer(fragmentToolbar)
                    }
                    else -> {
                        activityToolbar.visibility = View.VISIBLE
                        setSupportActionBar(activityToolbar)
                        disableDrawer(activityToolbar)
                    }
                }
                true
            }
        } else {
            activityToolbar.visibility = View.VISIBLE
            setSupportActionBar(activityToolbar)
            initDrawer(activityToolbar)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
        const val FRAGMENT_DETAILS = "FragmentDetails"
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
            .commit()
    }

    private fun openList() {
        val ft = supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, MovieListFragment(), MovieListFragment.TAG)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
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

    override fun changeTitle(title: String) {
        supportActionBar?.title = title
    }
}
