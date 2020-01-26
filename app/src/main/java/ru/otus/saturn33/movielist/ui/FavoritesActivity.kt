package ru.otus.saturn33.movielist.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.otus.saturn33.movielist.R
import ru.otus.saturn33.movielist.data.MovieDTO
import ru.otus.saturn33.movielist.data.Storage
import ru.otus.saturn33.movielist.ui.adapters.FavoritesListAdapter

class FavoritesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_favorites)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initRecycler()
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
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter =
            FavoritesListAdapter(
                LayoutInflater.from(this),
                Storage.movies.filter { it.inFav } as MutableList<MovieDTO>,
                colorPair)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
