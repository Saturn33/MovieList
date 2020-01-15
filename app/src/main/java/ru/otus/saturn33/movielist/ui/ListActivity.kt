package ru.otus.saturn33.movielist.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ru.otus.saturn33.movielist.R
import ru.otus.saturn33.movielist.data.ReactionDTO
import ru.otus.saturn33.movielist.data.Storage

class ListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        for (i in 0..2) {
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

        findViewById<ImageButton>(R.id.invite).setOnClickListener {
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
        for (i in 0..2) {
            try {
                val movie = Storage.movies.elementAt(i)
                val text = findViewById<TextView>(Storage.getTextId(i))
                if (movie.checked) {
                    text.setTextColor(resources.getColor(R.color.colorAccent, theme))
                }
            } catch (e: IndexOutOfBoundsException) {
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.let {
                val reaction = it.getParcelableExtra<ReactionDTO>(REACTION_KEY) ?: ReactionDTO()
                Log.d(TAG, "Liked: ${reaction.liked}")
                Log.d(TAG, "Comment: ${reaction.comment}")
            }
        }
    }

    companion object {
        const val TAG = "TST"
        const val REQUEST_CODE = 0
        const val REACTION_KEY = "reaction"
    }
}
