package ru.otus.saturn33.movielist.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import ru.otus.saturn33.movielist.R
import ru.otus.saturn33.movielist.data.ReactionDTO
import ru.otus.saturn33.movielist.data.Storage
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

        for (i in 0..3) {
            try {
                val movie = Storage.movies.elementAt(i)
                val btn = findViewById<Button>(Storage.getButtonId(i))
                val img = findViewById<ImageView>(Storage.getImageViewId(i))
                val text = findViewById<TextView>(Storage.getTextId(i))
                btn.visibility = View.VISIBLE
                img.visibility = View.VISIBLE
                text.visibility = View.VISIBLE
                img.setImageResource(Storage.getImageId(i))
                text.text = movie.name
                btn.setOnClickListener {
                    text.setTextColor(resources.getColor(R.color.colorAccent, theme))
                    movie.checked = true
                    startActivityForResult(Intent(this, DetailActivity::class.java).apply {
                        putExtra("movie", movie)
                    }, REQUEST_CODE)
                }
            } catch (e: IndexOutOfBoundsException) {
                findViewById<Button>(Storage.getButtonId(i)).visibility = View.GONE
                findViewById<ImageView>(Storage.getImageViewId(i)).visibility = View.GONE
                findViewById<TextView>(Storage.getTextId(i)).visibility = View.GONE
            }
        }

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
    }

    override fun onResume() {
        super.onResume()
        resumeSelection()
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
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun resumeSelection() {
        for (i in 0..3) {
            try {
                val movie = Storage.movies.elementAt(i)
                if (movie.checked) {
                    findViewById<TextView>(Storage.getTextId(i)).setTextColor(
                        resources.getColor(
                            R.color.colorAccent,
                            theme
                        )
                    )
                }
            } catch (e: IndexOutOfBoundsException) {
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.let {
                val reaction = it.getParcelableExtra(REACTION_KEY) ?: ReactionDTO()
                Log.d(TAG, "Liked: ${reaction.liked}")
                Log.d(TAG, "Comment: ${reaction.comment}")
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
        const val REQUEST_CODE = 0
        const val REACTION_KEY = "reaction"
        const val THEME_KEY = "theme"
    }
}
