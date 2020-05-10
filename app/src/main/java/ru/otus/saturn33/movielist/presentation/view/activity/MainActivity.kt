package ru.otus.saturn33.movielist.presentation.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import ru.otus.saturn33.movielist.R
import ru.otus.saturn33.movielist.data.entity.MovieDTO
import ru.otus.saturn33.movielist.presentation.dialog.ExitDialog
import ru.otus.saturn33.movielist.presentation.idlingresource.SimpleIdlingResource
import ru.otus.saturn33.movielist.presentation.interfaces.ActionBarProvider
import ru.otus.saturn33.movielist.presentation.notification.NotificationHelper
import ru.otus.saturn33.movielist.presentation.notification.NotificationHelper.DETAILED_REQUEST_ACTION
import ru.otus.saturn33.movielist.presentation.notification.NotificationHelper.POSTPONE_REQUEST_ACTION
import ru.otus.saturn33.movielist.presentation.view.fragment.MovieDetailFragment
import ru.otus.saturn33.movielist.presentation.view.fragment.MovieFavoritesFragment
import ru.otus.saturn33.movielist.presentation.view.fragment.MovieListFragment
import ru.otus.saturn33.movielist.presentation.view.fragment.MoviePostponedFragment
import ru.otus.saturn33.movielist.presentation.viewmodel.MovieListViewModel
import ru.otus.saturn33.movielist.presentation.viewmodel.MovieListViewModelFactory

class MainActivity : AppCompatActivity(), MovieListFragment.OnDetailedClickListener,
    MovieFavoritesFragment.OnDetailedClickListener,
    MoviePostponedFragment.OnDetailedClickListener,
    NavigationView.OnNavigationItemSelectedListener,
    ActionBarProvider {
    private var themeMode = AppCompatDelegate.MODE_NIGHT_NO
    private lateinit var mFirebaseAnalytics: FirebaseAnalytics
    private lateinit var mFirebaseRemoteConfig: FirebaseRemoteConfig
    @VisibleForTesting
    val idlingResource = SimpleIdlingResource()

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
        idlingResource.setIdleState(false)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config)

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

        if (savedInstanceState == null && intent?.action != DETAILED_REQUEST_ACTION && intent?.action != POSTPONE_REQUEST_ACTION) {
            checkConfig()
        }
        updateToolBar(activityToolbar)
        initDrawer(findViewById(R.id.toolbar))
    }

    override fun onResume() {
        super.onResume()
        checkIntent(intent)
    }

    private fun checkConfig() {
        mFirebaseRemoteConfig.fetch(3).addOnCompleteListener { taskFetch ->
            if (taskFetch.isSuccessful) {
                mFirebaseRemoteConfig.activate().addOnCompleteListener { taskActivation ->
                    if (taskActivation.isSuccessful) {
                        val startScreen = mFirebaseRemoteConfig.getString("starting_screen")
                        Log.d("REMOTE!", startScreen)
                        when (startScreen) {
                            "favorites" -> {
                                openList()
                                openFavorites()
                            }
                            "postponed" -> {
                                openList()
                                openPostponed()
                            }
                            "list" -> openList()
                            else -> openList()
                        }
                        idlingResource.setIdleState(true)
                    }
                }
            }
        }
    }

    private fun checkIntent(intent: Intent?): Boolean {
        intent?.let {
            return when (it.action) {
                POSTPONE_REQUEST_ACTION,
                DETAILED_REQUEST_ACTION -> {
                    val movie = it.extras?.getParcelable<MovieDTO?>(NotificationHelper.EXTRA_MOVIE)
                    openList()
                    openDetailed(movie)
                    true
                }
                else -> false
            }
        }
        return false
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
                    MovieDetailFragment.TAG -> {
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
    }

    override fun onDetailedClick(item: MovieDTO) {
        openDetailed(item)
    }

    private fun openDetailed(item: MovieDTO? = null) {
        if (item != null) {
            ViewModelProvider(this, MovieListViewModelFactory()).get(
                MovieListViewModel::class.java
            ).onMovieSelect(item)
            val bundle = Bundle().apply {
                putString(FirebaseAnalytics.Param.ITEM_ID, item.id.toString())
                putString(FirebaseAnalytics.Param.ITEM_NAME, item.name)
                putString(FirebaseAnalytics.Param.CONTENT_TYPE, "movie detail")
            }
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle)
        }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, MovieDetailFragment(), MovieDetailFragment.TAG)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(FRAGMENT_DETAILS)
            .commit()
    }

    private fun openFavorites() {
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.CONTENT_TYPE, "favorites list")
        }
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM_LIST, bundle)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, MovieFavoritesFragment(), MovieFavoritesFragment.TAG)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    private fun openPostponed() {
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.CONTENT_TYPE, "postponed list")
        }
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM_LIST, bundle)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, MoviePostponedFragment(), MoviePostponedFragment.TAG)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    private fun openList() {
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.CONTENT_TYPE, "main movie list")
        }
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM_LIST, bundle)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, MovieListFragment(), MovieListFragment.TAG)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_list -> openList()
            R.id.nav_favorite -> openFavorites()
            R.id.nav_postpone -> openPostponed()
        }
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)

        return true
    }

    override fun changeTitle(title: String) {
        supportActionBar?.title = title
    }
}
