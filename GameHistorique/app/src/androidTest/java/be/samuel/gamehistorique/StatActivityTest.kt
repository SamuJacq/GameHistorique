package be.samuel.gamehistorique

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import be.samuel.gamehistorique.activity.MainActivity
import be.samuel.gamehistorique.activity.StatActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
@LargeTest
class StatActivityTest {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(StatActivity::class.java)

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun backMainToStat(){
        Espresso.onView(withId(R.id.back_button)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(MainActivity::class.java.name))
    }

    @Test
    fun createRecyclerView(){
        Espresso.onView(withId(R.id.input_game_stat)).perform(ViewActions.replaceText("échec"))

        Espresso.onView(withId(R.id.recherche_button)).perform(click())

        Espresso.onView(withId(R.id.recycler_view_player)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(withId(R.id.recycler_view_player)).check(RecyclerViewItemCountAssertion(6))

    }

}

class RecyclerViewItemCountAssertion(private val expectedCount: Int) : ViewAssertion {

    override fun check(view: android.view.View?, noViewFoundException: NoMatchingViewException?) {
        if (view is RecyclerView) {
            val adapter = view.adapter
            val itemCount = adapter?.itemCount ?: 0
            if (itemCount != expectedCount) {
                throw AssertionError("Expected $expectedCount items, but found $itemCount")
            }
        } else {
            throw AssertionError("The asserted view is not a RecyclerView")
        }
    }
}