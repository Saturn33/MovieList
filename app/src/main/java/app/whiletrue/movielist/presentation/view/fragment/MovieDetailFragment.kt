package app.whiletrue.movielist.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import app.whiletrue.movielist.R
import app.whiletrue.movielist.data.entity.MovieDTO
import app.whiletrue.movielist.presentation.viewmodel.MovieListViewModel
import app.whiletrue.movielist.presentation.viewmodel.MovieListViewModelFactory
import com.bumptech.glide.Glide

class MovieDetailFragment : Fragment() {
    private var viewModel: MovieListViewModel? = null

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

        viewModel = activity?.let {
            ViewModelProvider(it, MovieListViewModelFactory()).get(
                MovieListViewModel::class.java
            )
        }
        viewModel?.selectedMovie?.observe(this.viewLifecycleOwner, Observer<MovieDTO> {
            showMovieData(view, it)
        })
    }

    private fun showMovieData(view: View, movie: MovieDTO) {
        view.findViewById<Toolbar>(R.id.toolbarAdvanced)?.title = movie.name

        val img: ImageView = view.findViewById(R.id.image)
        Glide.with(view)
            .load(movie.imageURL)
            .placeholder(R.drawable.movie_filler)
            .fallback(R.drawable.movie_filler)
            .error(R.drawable.movie_filler)
            .into(img)
        img.visibility = if (movie.imagePath != null) View.VISIBLE else View.INVISIBLE

        view.findViewById<TextView>(R.id.description).text = movie.description
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                childFragmentManager.popBackStack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val TAG = "MovieDetail"
    }
}
