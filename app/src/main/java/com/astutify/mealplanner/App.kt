package com.astutify.mealplanner

import android.app.Application
import com.astutify.mealplanner.auth.AuthComponent
import com.astutify.mealplanner.auth.AuthComponentProvider
import com.astutify.mealplanner.ingredient.IngredientComponent
import com.astutify.mealplanner.ingredient.IngredientComponentProvider
import com.astutify.mealplanner.recipe.RecipeComponent
import com.astutify.mealplanner.recipe.RecipeComponentProvider
import com.astutify.mealplanner.userprofile.UserProfileComponent
import com.astutify.mealplanner.userprofile.UserProfileComponentProvider

class App :
    Application(),
    IngredientComponentProvider,
    RecipeComponentProvider,
    AuthComponentProvider,
    UserProfileComponentProvider {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    private val componentManager = ComponentManager(this)

    override val ingredientComponent: IngredientComponent
        get() = componentManager.ingredientComponent

    override val recipeComponent: RecipeComponent
        get() = componentManager.recipeComponent

    override val authComponent: AuthComponent
        get() = componentManager.authComponent

    override val userProfileComponent: UserProfileComponent
        get() = componentManager.userProfileComponent

    companion object {
        private lateinit var instance: App

        fun get(): App {
            return instance
        }
    }
}
