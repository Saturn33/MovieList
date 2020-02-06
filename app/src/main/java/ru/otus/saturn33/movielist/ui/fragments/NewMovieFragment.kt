package ru.otus.saturn33.movielist.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import ru.otus.saturn33.movielist.R
import ru.otus.saturn33.movielist.data.MovieDTO

class NewMovieFragment : Fragment() {
    private var listener: OnNewMovieClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_new, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.findViewById<Toolbar>(R.id.toolbar)?.title = getString(R.string.title_new_movie)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_new, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_new_done -> {
                val validationResult = validateInput()
                if (validationResult.first) {
                    listener?.onNewMovieClick(validationResult.second ?: MovieDTO("", "", null))
                    fragmentManager?.popBackStack()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun validateInput(): Pair<Boolean, MovieDTO?> {
        val title: String
        val description: String

        val titleTv = view?.findViewById<EditText>(R.id.title) ?: return Pair(false, null)
        if (titleTv.text.isEmpty()) {
            titleTv.error = resources.getString(R.string.empty_title)
            return Pair(false, null)
        } else {
            title = titleTv.text.toString()
        }

        val descriptionTv =
            view?.findViewById<EditText>(R.id.description) ?: return Pair(false, null)
        if (descriptionTv.text.isEmpty()) {
            descriptionTv.error = resources.getString(R.string.empty_description)
            return Pair(false, null)
        } else {
            description = descriptionTv.text.toString()
        }

        return Pair(true, MovieDTO(title, description, null))
    }

    companion object {
        const val TAG = "MovieNew"
    }

    interface OnNewMovieClickListener {
        fun onNewMovieClick(item: MovieDTO)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (activity is OnNewMovieClickListener) {
            listener = activity as OnNewMovieClickListener
        }
    }
}
