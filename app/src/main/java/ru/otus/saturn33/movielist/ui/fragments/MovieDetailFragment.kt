package ru.otus.saturn33.movielist.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import ru.otus.saturn33.movielist.R
import ru.otus.saturn33.movielist.data.MovieDTO

class MovieDetailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_detail, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movie = arguments?.getParcelable(EXTRA_ITEM) ?: MovieDTO("", "", 0)

        view.findViewById<Toolbar>(R.id.toolbarAdvanced)?.title = movie.name

        val img: ImageView? = activity?.findViewById(R.id.image)
        if (movie.imageId != null) {
            img?.setImageResource(movie.imageId)
            img?.visibility = View.VISIBLE
        } else {
            img?.setImageResource(R.drawable.movie_filler)
            img?.visibility = View.INVISIBLE
        }

        view.findViewById<TextView>(R.id.description).text = movie.description
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                fragmentManager?.popBackStack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val TAG = "MovieDetail"
        const val EXTRA_ITEM = "EXTRA_ITEM"

        fun newInstance(item: MovieDTO): MovieDetailFragment {
            val fragment = MovieDetailFragment()
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_ITEM, item)
            fragment.arguments = bundle

            return fragment
        }

    }
}
