package com.astutify.mealplanner.ingredient.presentation.recipeingredient

import android.content.Intent
import android.content.res.Resources
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressKey
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import com.astutify.mealplanner.MockApp
import com.astutify.mealplanner.coreui.model.IngredientViewModel
import com.astutify.mealplanner.coreui.presentation.utils.TestHelper
import com.astutify.mealplanner.ingredient.R
import com.astutify.mealplanner.ingredient.presentation.recipeingredient.mvi.RecipeIngredientsViewState
import com.astutify.mealplanner.utils.RecyclerViewItemCountAssertion.Companion.withItemCount
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.observers.TestObserver
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import com.astutify.mealplanner.ingredient.presentation.recipeingredient.RecipeIngredientsView as RecipeIngredientsView1

class RecipeIngredientActivityTest {

    private var controller: RecipeIngredientsViewController = mock()
    private val ingredientGroup = "ingredientGroup"

    @Rule
    @JvmField
    var activityRule: ActivityTestRule<RecipeIngredientActivity> =
        object : ActivityTestRule<RecipeIngredientActivity>(RecipeIngredientActivity::class.java) {

            override fun getActivityIntent(): Intent {
                return Intent().apply {
                    putExtra(RecipeIngredientActivity.IngredientGroupIdExtra, ingredientGroup)
                }
            }

            override fun beforeActivityLaunched() {
                ApplicationProvider.getApplicationContext<MockApp>().ingredientComponent = mock {
                    on { recipeIngredientActivityBuilder() } doReturn WelcomeTutorialComponentBuilderMock(
                        controller
                    )
                }
            }
        }

    private fun runOnUiThread(runnable: () -> Unit) {
        activityRule.runOnUiThread(runnable)
    }

    private val view: RecipeIngredientsView1
        get() = activityRule.activity

    @Test
    fun renderIngredients() {
        val ingredients = mutableListOf<IngredientViewModel>()
        for (i in 1..5) {
            ingredients.add(TestHelper.getIngredientVM())
        }
        val state =
            RecipeIngredientsViewState(
                ingredients = ingredients
            )

        runOnUiThread {
            view.render(state)
        }

        onView(withId(R.id.searchResults)).check(matches(isDisplayed())).check(withItemCount(6))
        onView(withId(R.id.searchField)).check(matches(isDisplayed()))
    }

    @Test
    fun clickAddIngredient() {
        val ingredients = listOf(TestHelper.getIngredientVM())
        val testObserver = TestObserver<RecipeIngredientsView1.Intent>()
        val state =
            RecipeIngredientsViewState(
                ingredients = ingredients
            )

        runOnUiThread {
            view.render(state)
            view.events.subscribe(testObserver)
        }

        onView(withText(R.string.add_ingredient)).perform(click())
        assert(testObserver.values().contains(RecipeIngredientsView1.Intent.ClickAddIngredient))
    }

    @Test
    fun endOfListReached() {
        val ingredients = listOf(TestHelper.getIngredientVM())
        val testObserver = TestObserver<RecipeIngredientsView1.Intent>()
        val state =
            RecipeIngredientsViewState(
                ingredients = ingredients
            )

        runOnUiThread {
            view.render(state)
            view.events.subscribe(testObserver)
        }

        assert(testObserver.values().contains(RecipeIngredientsView1.Intent.EnOfListReached))
    }

    @Test
    fun sendEventWhenWritesOnSearchBox() {
        val state =
            RecipeIngredientsViewState()
        val searchText = "searchText"
        val testObserver = TestObserver<RecipeIngredientsView1.Intent>()

        runOnUiThread {
            view.render(state)
            view.events.subscribe(testObserver)
        }

        Thread.sleep(2000)

        onView(withId(R.id.searchField)).perform(click())
        onView(
            withId(
                Resources.getSystem().getIdentifier(
                    "search_src_text",
                    "id", "android"
                )
            )
        ).perform(clearText(), typeText("enter the text"))
            .perform(pressKey(KeyEvent.KEYCODE_ENTER))
        // onView(isAssignableFrom(androidx.appcompat.widget.SearchView::class.java)).perform(typeText(searchText))
        testObserver.assertValue(RecipeIngredientsView1.Intent.Search(searchText))
    }

    @Test
    fun ingredientQuantitySet() {
        val ingredient = TestHelper.getIngredientVM()
        val state =
            RecipeIngredientsViewState(
                selectedIngredient = ingredient
            )
        val testObserver = TestObserver<RecipeIngredientsView1.Intent>()

        runOnUiThread {
            view.render(state)
            view.events.subscribe(testObserver)
        }
        onView(withId(R.id.quantity)).perform(click(), typeText("893"))
        onView(withText(R.string.save)).perform(click())

        val result =
            testObserver.values().find { it is RecipeIngredientsView1.Intent.IngredientQuantitySet }
        assertEquals(893f, (result as RecipeIngredientsView1.Intent.IngredientQuantitySet).quantity)
        assertEquals(TestHelper.getPrimaryMeasurementVM(), (result).measurement)
    }

    @Test
    fun clickBack() {
        val testObserver = TestObserver<RecipeIngredientsView1.Intent>()

        runOnUiThread {
            view.events.subscribe(testObserver)
        }
        onView(
            allOf(
                instanceOf(AppCompatImageButton::class.java),
                withParent(withId(R.id.toolbar))
            )
        ).perform(click())

        val result = testObserver.values().find { it is RecipeIngredientsView1.Intent.ClickBack }
        assert(result is RecipeIngredientsView1.Intent.ClickBack)
    }

    @Test
    fun clickIngredient() {
        val ingredients = mutableListOf<IngredientViewModel>()
        for (i in 1..2) {
            ingredients.add(TestHelper.getIngredientVM())
        }
        val state =
            RecipeIngredientsViewState(
                ingredients = ingredients
            )
        val testObserver = TestObserver<RecipeIngredientsView1.Intent>()

        runOnUiThread {
            view.render(state)
            view.events.subscribe(testObserver)
        }

        onView(withId(R.id.searchResults)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        val result =
            testObserver.values().find { it is RecipeIngredientsView1.Intent.IngredientClick }
        assert((result as RecipeIngredientsView1.Intent.IngredientClick).ingredient == TestHelper.getIngredientVM())
    }
}

private class WelcomeTutorialComponentBuilderMock(
    var controller: RecipeIngredientsViewController
) : RecipeIngredientActivity.Component.Builder {

    override fun with(view: RecipeIngredientsView1): RecipeIngredientActivity.Component.Builder =
        this

    override fun withActivity(view: AppCompatActivity): RecipeIngredientActivity.Component.Builder =
        this

    override fun ingredientGroupId(id: String): RecipeIngredientActivity.Component.Builder = this

    override fun build(): RecipeIngredientActivity.Component =
        object : RecipeIngredientActivity.Component {

            override fun inject(fragment: RecipeIngredientActivity) {
                fragment.controller = controller
            }
        }
}
