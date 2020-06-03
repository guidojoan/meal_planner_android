package com.astutify.mealplanner

import android.app.Application
import com.astutify.mealplanner.ingredient.IngredientComponent
import com.astutify.mealplanner.ingredient.IngredientComponentProvider

class MockApp : Application(), IngredientComponentProvider {
    override lateinit var ingredientComponent: IngredientComponent
}
