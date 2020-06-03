package com.astutify.mealplanner

import com.astutify.mealplanner.auth.AuthComponent
import com.astutify.mealplanner.auth.DaggerAuthComponent
import com.astutify.mealplanner.core.CoreComponent
import com.astutify.mealplanner.core.DaggerCoreComponent
import com.astutify.mealplanner.ingredient.DaggerIngredientComponent
import com.astutify.mealplanner.ingredient.IngredientComponent
import com.astutify.mealplanner.recipe.DaggerRecipeComponent
import com.astutify.mealplanner.recipe.RecipeComponent
import com.astutify.mealplanner.userprofile.DaggerUserProfileComponent
import com.astutify.mealplanner.userprofile.UserProfileComponent

class ComponentManager(
    val app: App
) {

    private val coreComponent: CoreComponent by lazy {
        DaggerCoreComponent
            .builder()
            .withContext(app)
            .build()
    }

    val ingredientComponent: IngredientComponent by lazy {
        DaggerIngredientComponent
            .builder()
            .withCoreComponent(coreComponent)
            .build()
    }

    private val navigationComponent: NavigationComponent by lazy {
        DaggerNavigationComponent
            .builder()
            .build()
    }

    val authComponent: AuthComponent by lazy {
        DaggerAuthComponent
            .builder()
            .withCoreComponent(coreComponent)
            .withNavigatorComponent(navigationComponent)
            .build()
    }

    val recipeComponent: RecipeComponent by lazy {
        DaggerRecipeComponent
            .builder()
            .withCoreComponent(coreComponent)
            .withNavigationComponent(navigationComponent)
            .build()
    }

    val userProfileComponent: UserProfileComponent by lazy {
        DaggerUserProfileComponent
            .builder()
            .withCoreComponent(coreComponent)
            .withNavigatorComponent(navigationComponent)
            .build()
    }
}
