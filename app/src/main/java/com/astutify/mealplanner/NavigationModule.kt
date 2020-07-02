package com.astutify.mealplanner

import com.astutify.mealplanner.auth.AuthOutNavigator
import com.astutify.mealplanner.recipe.RecipeOutNavigator
import com.astutify.mealplanner.userprofile.UserProfileOutNavigator
import dagger.Binds
import dagger.Module

@Module
abstract class NavigationModule {

    @Binds
    abstract fun authOutNavigatorProvider(factory: AuthOutNavigatorImpl.Factory): AuthOutNavigator.Factory

    @Binds
    abstract fun recipeOutNavigatorProvider(factory: RecipeOutNavigatorImpl.Factory): RecipeOutNavigator.Factory

    @Binds
    abstract fun profileOutNavigator(factory: UserProfileOutNavigatorImpl.Factory): UserProfileOutNavigator.Factory
}
