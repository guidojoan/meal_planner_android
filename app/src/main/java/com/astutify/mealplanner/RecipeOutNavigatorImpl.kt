package com.astutify.mealplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.astutify.mealplanner.auth.presentation.login.LoginActivity
import com.astutify.mealplanner.ingredient.presentation.recipeingredient.RecipeIngredientActivity
import com.astutify.mealplanner.ingredient.presentation.recipeingredient.RecipeIngredientActivity.Companion.IngredientGroupIdExtra
import com.astutify.mealplanner.recipe.RecipeOutNavigator
import com.astutify.mealplanner.recipe.presentation.edit.EditRecipeActivity.Companion.AddRecipeIngredientRequestCode
import com.astutify.mealplanner.userprofile.presentation.house.HouseEditActivity
import javax.inject.Inject

class RecipeOutNavigatorImpl constructor(
    private val activity: AppCompatActivity
) : RecipeOutNavigator {

    override fun goToLogin() {
        activity.startActivity(Intent(activity, LoginActivity::class.java))
        activity.finish()
    }

    override fun goToHouseEdit() {
        activity.startActivity(Intent(activity, HouseEditActivity::class.java))
        activity.finish()
    }

    override fun goToAddRecipeIngredient(ingredientGroupId: String) {
        val intent = Intent(activity, RecipeIngredientActivity::class.java)
        intent.putExtra(IngredientGroupIdExtra, ingredientGroupId)
        activity.startActivityForResult(
            intent,
            AddRecipeIngredientRequestCode
        )
    }

    class Factory @Inject constructor() : RecipeOutNavigator.Factory {
        override fun create(activity: AppCompatActivity): RecipeOutNavigator {
            return RecipeOutNavigatorImpl(activity)
        }
    }
}
