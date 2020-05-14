package app.whiletrue.movielist.presentation.view.activity

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import app.whiletrue.movielist.R
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Description
import org.hamcrest.Matcher


object RecyclerHelper {
    fun actionChildViewWithId(id: Int, action: ViewAction): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return allOf(isDisplayed(), isAssignableFrom(View::class.java))
            }

            override fun getDescription(): String {
                return "Perform action on a child view with specified id."
            }

            override fun perform(uiController: UiController?, view: View) {
                val v: View = view.findViewById(id)
                action.perform(uiController, v)
            }
        }
    }

    fun childViewAtPositionMatcher(id: Int, position: Int, matcher: Matcher<View>): Matcher<View> {
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description?) {
                description?.appendText("Checks matcher matches child view of viewholder on specified position")
            }

            override fun matchesSafely(recycler: RecyclerView): Boolean {
                val viewHolder = recycler.findViewHolderForAdapterPosition(position)
                val parentMatcher = hasDescendant(allOf(withId(id), matcher))
                return viewHolder != null && parentMatcher.matches(viewHolder.itemView)
            }
        }
    }

    fun inFavMatcher(): Matcher<View> {
        return object : BoundedMatcher<View, ImageView>(ImageView::class.java) {
            override fun describeTo(description: Description?) {
                description?.appendText("Checks imageview has proper inFav image resource")
            }

            override fun matchesSafely(item: ImageView?): Boolean {
                item?.let {
                    return it.tag == R.drawable.favorite_yes
                }
                return false
            }
        }
    }

}
