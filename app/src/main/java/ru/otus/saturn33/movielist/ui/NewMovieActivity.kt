package ru.otus.saturn33.movielist.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import ru.otus.saturn33.movielist.R
import ru.otus.saturn33.movielist.data.MovieDTO
import ru.otus.saturn33.movielist.ui.ListActivity.Companion.MOVIE_KEY

class NewMovieActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_movie)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_new, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_new_done -> {
                val validationResult = validateInput()
                if (validationResult.first) {
                    setResult(Activity.RESULT_OK, Intent().apply {
                        putExtra(MOVIE_KEY, validationResult.second)
                    })
                    finish()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun validateInput(): Pair<Boolean, MovieDTO?> {
        val title: String
        val description: String

        val titleTv = findViewById<EditText>(R.id.title)
        if (titleTv.text.isEmpty()) {
            titleTv.error = resources.getString(R.string.empty_title)
            return Pair(false, null)
        } else {
            title = titleTv.text.toString()
        }

        val descriptionTv = findViewById<EditText>(R.id.description)
        if (descriptionTv.text.isEmpty()) {
            descriptionTv.error = resources.getString(R.string.empty_description)
            return Pair(false, null)
        } else {
            description = descriptionTv.text.toString()
        }

        return Pair(true, MovieDTO(title, description, null))
    }

}
