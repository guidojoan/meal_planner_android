package com.astutify.mealplanner.ingredient

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.astutify.mealplanner.AppConstants
import com.astutify.mealplanner.core.Mockable
import com.astutify.mealplanner.coreui.model.ActivityResult
import com.astutify.mealplanner.coreui.model.IngredientViewModel
import com.astutify.mealplanner.coreui.model.RecipeIngredientViewModel
import com.astutify.mealplanner.ingredient.presentation.editingredient.EditIngredientActivity
import com.astutify.mealplanner.ingredient.presentation.recipeingredient.RecipeIngredientActivity
import javax.inject.Inject

@Mockable
class Navigator @Inject constructor(
    private val activity: AppCompatActivity
) {

    fun goToAddIngredient() {
        activity.startActivityForResult(
            Intent(activity, EditIngredientActivity::class.java),
            RecipeIngredientActivity.EditIngredientResultCode
        )
    }

    fun goToEditIngredient(ingredient: IngredientViewModel) {
        val intent = Intent(activity, EditIngredientActivity::class.java)
        intent.putExtra(EditIngredientActivity.INGREDIENT_EXTRA, ingredient)
        activity.startActivityForResult(intent, RecipeIngredientActivity.EditIngredientResultCode)
    }

    fun finishAddRecipeIngredient(result: ActivityResult<RecipeIngredientViewModel>) {
        val data = Intent()
        data.putExtra(AppConstants.ActivityResultObject, result)
        activity.setResult(Activity.RESULT_OK, data)
        activity.finish()
    }

    fun finishAddIngredient(result: ActivityResult<IngredientViewModel>) {
        val data = Intent()
        data.putExtra(AppConstants.ActivityResultObject, result)
        activity.setResult(Activity.RESULT_OK, data)
        activity.finish()
    }

    fun goBack() {
        activity.finish()
    }
}
