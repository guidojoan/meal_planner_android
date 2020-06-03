package com.astutify.mealplanner.recipe

import androidx.appcompat.app.AppCompatActivity
import com.astutify.mealplanner.core.Mockable

@Mockable
interface RecipeOutNavigator {

    fun goToLogin()
    fun goToAddRecipeIngredient(ingredientGroupId: String)
    fun goToHouseEdit()

    interface Factory {
        fun create(activity: AppCompatActivity): RecipeOutNavigator
    }
}
