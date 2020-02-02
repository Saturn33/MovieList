package ru.otus.saturn33.movielist.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import ru.otus.saturn33.movielist.R
import ru.otus.saturn33.movielist.data.MovieDTO
import ru.otus.saturn33.movielist.data.Storage
import ru.otus.saturn33.movielist.ui.dialogs.ExitDialog
import ru.otus.saturn33.movielist.ui.fragments.MovieDetailFragment
import ru.otus.saturn33.movielist.ui.fragments.MovieFavoritesFragment
import ru.otus.saturn33.movielist.ui.fragments.MovieListFragment

class MainActivity : AppCompatActivity(), MovieListFragment.OnClickListener,
    MovieFavoritesFragment.OnDetailedClickListener,
    NavigationView.OnNavigationItemSelectedListener {
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

        setContentView(R.layout.activity_main)

        val toolbar: Toolbar? = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        openList(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
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
            val dialog = ExitDialog(this)
            dialog.setOnCancelListener {
                super.onBackPressed()
            }
            dialog.show()
        }
    }


    companion object {
        const val THEME_KEY = "theme"
        const val FRAGMENT_LIST = "FragmentList"
        const val FRAGMENT_DETAILS = "FragmentDetails"
        const val FRAGMENT_FAVORITE = "FragmentFavorite"

        const val REQUEST_CODE_NEW_MOVIE = 0
        const val MOVIE_KEY = "movie"
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
        startActivityForResult(
            Intent(this, NewMovieActivity::class.java),
            REQUEST_CODE_NEW_MOVIE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_NEW_MOVIE -> data?.let {
                    val movie: MovieDTO? = it.getParcelableExtra(MOVIE_KEY)
                    movie?.let { movieDTO ->
                        Storage.movies.add(movieDTO)
                        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
                        recyclerView.adapter?.notifyItemInserted(Storage.movies.size)
                    }
                }
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_list -> openList()
            R.id.nav_favorite -> openFavorites()
        }
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)

        return true
    }
}
