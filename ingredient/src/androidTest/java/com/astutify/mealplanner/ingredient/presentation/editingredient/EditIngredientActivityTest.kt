package com.astutify.mealplanner.ingredient.presentation.editingredient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.test.core.app.ApplicationProvider
import androidx.test.rule.ActivityTestRule
import com.astutify.mealplanner.MockApp
import com.astutify.mealplanner.coreui.model.IngredientViewModel
import com.astutify.mealplanner.coreui.presentation.utils.TestHelper
import com.nhaarman.mockitokotlin2.mock
import org.junit.Rule

class EditIngredientActivityTest {

    private var controller: EditIngredientViewController = mock()
    private var ingredient = TestHelper.getIngredientVM()

    @Rule
    @JvmField
    var activityRule: ActivityTestRule<EditIngredientActivity> =
        object : ActivityTestRule<EditIngredientActivity>(EditIngredientActivity::class.java) {

            override fun getActivityIntent(): Intent {
                return Intent().apply {
                    putExtra(EditIngredientActivity.INGREDIENT_EXTRA, ingredient)
                }
            }

            override fun beforeActivityLaunched() {
                ApplicationProvider.getApplicationContext<MockApp>().ingredientComponent = mock {
                    on { editIngredientActivityBuilder() } doReturn ComponentBuilderMock(
                        controller
                    )
                }
            }
        }

    private fun runOnUiThread(runnable: () -> Unit) {
        activityRule.runOnUiThread(runnable)
    }

    private val view: EditIngredientView
        get() = activityRule.activity
}

private class ComponentBuilderMock(
    var controller: EditIngredientViewController
) : EditIngredientActivity.Component.Builder {

    override fun with(view: EditIngredientView): EditIngredientActivity.Component.Builder =
        this

    override fun withIngredient(view: IngredientViewModel?): EditIngredientActivity.Component.Builder =
        this

    override fun withInitialState(state: EditIngredientViewState?): EditIngredientActivity.Component.Builder =
        this

    override fun withActivity(view: AppCompatActivity): EditIngredientActivity.Component.Builder =
        this

    override fun build(): EditIngredientActivity.Component =
        object : EditIngredientActivity.Component {

            override fun inject(fragment: EditIngredientActivity) {
                fragment.controller = controller
            }
        }
}
