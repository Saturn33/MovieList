package ru.otus.saturn33.movielist.presentation.view.activity

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.otus.saturn33.movielist.R


@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    private var mIdlingResource: IdlingResource? = null

    @Test
    fun clickFavoritesIcon_Changes_FavoriteState_Test() {

        val position = 2
        var alreadyInFav = false

        // Check its already in favorites
        onView(withId(R.id.recyclerView))
            .withFailureHandler { _, _ ->
                alreadyInFav = true
            }
            .check(
                matches(
                    RecyclerHelper.childViewAtPositionMatcher(
                        R.id.inFavIv,
                        position,
                        not(RecyclerHelper.inFavMatcher())
                    )
                )
            )

        // Click favorites icon
        onView(withId(R.id.recyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    position,
                    RecyclerHelper.actionChildViewWithId(R.id.inFavIv, click())
                )
            )

        // Match inFav
        onView(withId(R.id.recyclerView))
            .check(
                matches(
                    RecyclerHelper.childViewAtPositionMatcher(
                        R.id.inFavIv,
                        position,
                        if (alreadyInFav) not(RecyclerHelper.inFavMatcher()) else RecyclerHelper.inFavMatcher()
                    )
                )
            )

    }

    @Before
    fun registerIdlingResource() {
        activityScenarioRule.scenario.onActivity { activity ->
            mIdlingResource = activity.idlingResource
            IdlingRegistry.getInstance().register(mIdlingResource)
        }
    }

    @After
    fun unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource)
        }
    }

}
