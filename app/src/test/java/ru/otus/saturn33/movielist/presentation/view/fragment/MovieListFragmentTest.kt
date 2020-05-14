package ru.otus.saturn33.movielist.presentation.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.android.synthetic.main.fragment_movie_list.*
import org.hamcrest.Description
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import ru.otus.saturn33.movielist.R


@RunWith(AndroidJUnit4::class)
class MovieListFragmentTest {

    @Test(expected = ArrayIndexOutOfBoundsException::class)
    fun testExplicitException() {

        val fragmentArgs = Bundle()
        val scenario = launchFragmentInContainer<MovieListFragment>(fragmentArgs)

        scenario.onFragment { fragment ->
            fragment.make_error.performClick()
        }
    }


    @Test
    fun testAfterSwipe_Is_In_Refreshing_State() {
        val fragmentArgs = Bundle()
        launchFragmentInContainer<MovieListFragment>(fragmentArgs)

        onView(withId(R.id.recyclerView)).perform(swipeDown())
        onView(withId(R.id.swipeRefresher)).check(matches(object :
            BoundedMatcher<View, SwipeRefreshLayout>(SwipeRefreshLayout::class.java) {
            override fun describeTo(description: Description?) {
                description?.appendText("is refreshing")
            }

            override fun matchesSafely(item: SwipeRefreshLayout?): Boolean {
                return item?.isRefreshing ?: false
            }
        }))
    }

    @Test
    fun testInvite() {

        val fragmentArgs = Bundle()
        val scenario = launchFragmentInContainer<MovieListFragment>(fragmentArgs)

        scenario.onFragment { fragment ->
            fragment.invite.performClick()

            val expectedIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, fragment.getString(R.string.try_app))
            }
            val shadowActivity = shadowOf(fragment.activity)
            val startedIntent = shadowActivity.nextStartedActivity

            Assert.assertEquals(expectedIntent.action, startedIntent.action)
            Assert.assertEquals(
                expectedIntent.extras?.get(Intent.EXTRA_TEXT),
                startedIntent.extras?.get(Intent.EXTRA_TEXT)
            )
        }
    }
}
