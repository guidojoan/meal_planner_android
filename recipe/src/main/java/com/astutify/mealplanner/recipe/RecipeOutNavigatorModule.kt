package com.astutify.mealplanner.recipe

import androidx.appcompat.app.AppCompatActivity
import dagger.Module
import dagger.Provides

@Module
abstract class RecipeOutNavigatorModule {

    @Module
    companion object {

        @JvmStatic
        @Provides
        fun recipeNavigationFactoryProvider(
            activity: AppCompatActivity,
            factory: RecipeOutNavigator.Factory
        ): RecipeOutNavigator {
            return factory.create(activity)
        }
    }
}
