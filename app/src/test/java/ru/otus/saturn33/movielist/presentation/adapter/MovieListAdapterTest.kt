package ru.otus.saturn33.movielist.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import ru.otus.saturn33.movielist.data.entity.MovieDTO

@RunWith(RobolectricTestRunner::class)
class MovieListAdapterTest {
    val context = ApplicationProvider.getApplicationContext<Context>()
    var movieListAdapter: MovieListAdapter? = null

    //    private lateinit var mockObserver: And
    @Test
    fun getEmptyItems() {
        val items = movieListAdapter?.items
        assertEquals(mutableListOf<MovieDTO>(), items)
    }

    @Test
    fun setItemsAndCheckCount() {
        val items = mutableListOf<MovieDTO>()
        repeat(5) {
            items.add(MovieDTO(it, it.toString(), it.toString(), it.toDouble(), postponeMillis = null))
        }

        movieListAdapter?.setItems(items)
        assertEquals(items.size + 1, movieListAdapter?.itemCount)
    }

    @Test
    fun clearItemsAndCheckCount() {
        val items = mutableListOf<MovieDTO>()
        repeat(5) {
            items.add(MovieDTO(it, it.toString(), it.toString(), it.toDouble(), postponeMillis = null))
        }

        movieListAdapter?.setItems(items)
        movieListAdapter?.clearItems()

        assertEquals(1, movieListAdapter?.itemCount)
    }

    @Test
    fun getItemViewType_Last_Is_Footer() {
        val items = mutableListOf<MovieDTO>()
        repeat(5) {
            items.add(MovieDTO(it, it.toString(), it.toString(), it.toDouble(), postponeMillis = null))
        }

        movieListAdapter?.setItems(items)

        assertEquals(
            MovieListAdapter.VIEW_TYPE_FOOTER,
            movieListAdapter?.getItemViewType(items.size)
        )
    }

    @Test
    fun getItemViewType_NotLast_Is_Item() {
        val items = mutableListOf<MovieDTO>()
        repeat(5) {
            items.add(MovieDTO(it, it.toString(), it.toString(), it.toDouble(), postponeMillis = null))
        }

        movieListAdapter?.setItems(items)

        assertEquals(MovieListAdapter.VIEW_TYPE_ITEM, movieListAdapter?.getItemViewType(0))
    }

    @Before
    fun setUp() {
//        val inflater = Mockito.mock(LayoutInflater::class.java)
        val inflater = LayoutInflater.from(context)
        movieListAdapter = MovieListAdapter(inflater, Pair(0, 0))
    }

    @After
    fun tearDown() {
        movieListAdapter = null
    }

}
