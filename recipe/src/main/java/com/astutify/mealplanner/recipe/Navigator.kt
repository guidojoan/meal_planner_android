package com.astutify.mealplanner.recipe

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.astutify.mealplanner.AppConstants
import com.astutify.mealplanner.core.Mockable
import com.astutify.mealplanner.coreui.entity.ActivityResult
import com.astutify.mealplanner.coreui.entity.RecipeViewModel
import com.astutify.mealplanner.recipe.presentation.detail.RecipeDetailViewActivity
import com.astutify.mealplanner.recipe.presentation.edit.EditRecipeActivity
import javax.inject.Inject

@Mockable
class Navigator @Inject constructor(
    private val activity: AppCompatActivity
) {

    fun goToAddRecipe() {
        val intent = Intent(activity, EditRecipeActivity::class.java)
        activity.startActivityForResult(intent, RecipeDetailViewActivity.EditRecipeActivityResult)
    }

    fun goToRecipeDetail(recipe: RecipeViewModel) {
        val intent = Intent(activity, RecipeDetailViewActivity::class.java)
        intent.putExtra(RecipeDetailViewActivity.RECIPE_EXTRA, recipe)
        activity.startActivity(intent)
    }

    fun goToEditRecipe(recipe: RecipeViewModel) {
        val intent = Intent(activity, EditRecipeActivity::class.java)
        intent.putExtra(RecipeDetailViewActivity.RECIPE_EXTRA, recipe)
        activity.startActivityForResult(intent, RecipeDetailViewActivity.EditRecipeActivityResult)
    }

    fun finishEditRecipe(result: ActivityResult<RecipeViewModel>) {
        val data = Intent()
        data.putExtra(AppConstants.ActivityResultObject, result)
        activity.setResult(Activity.RESULT_OK, data)
        activity.finish()
    }

    fun goBack() {
        activity.finish()
    }
}
