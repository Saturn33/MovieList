package ru.otus.saturn33.movielist.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ru.otus.saturn33.movielist.R
import ru.otus.saturn33.movielist.data.MovieDTO
import ru.otus.saturn33.movielist.data.ReactionDTO
import ru.otus.saturn33.movielist.ui.ListActivity.Companion.MOVIE_KEY
import ru.otus.saturn33.movielist.ui.ListActivity.Companion.REACTION_KEY

class DetailActivity : AppCompatActivity() {
    val reaction = ReactionDTO()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val movie = intent.getParcelableExtra(MOVIE_KEY) ?: MovieDTO("", "", 0)

        title = movie.name
        if (movie.imageId != null)
            findViewById<ImageView>(R.id.image_detail).setImageResource(movie.imageId)
        findViewById<TextView>(R.id.description).text = movie.description

        setResult(Activity.RESULT_OK, Intent().apply {
            putExtra(REACTION_KEY, reaction)
        })

        val liked = findViewById<CheckBox>(R.id.liked)
        liked.setOnClickListener {
            reaction.liked = liked.isChecked
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(REACTION_KEY, reaction)
            })
        }

        findViewById<EditText>(R.id.comment).addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                reaction.comment = s.toString()
                setResult(Activity.RESULT_OK, Intent().apply {
                    putExtra(REACTION_KEY, reaction)
                })
            }
        })
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
