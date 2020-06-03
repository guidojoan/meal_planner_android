package com.astutify.mealplanner.recipe

import com.astutify.mealplanner.core.CoreComponent
import com.astutify.mealplanner.core.FeatureScope
import com.astutify.mealplanner.recipe.presentation.detail.RecipeDetailViewActivity
import com.astutify.mealplanner.recipe.presentation.edit.EditRecipeActivity
import com.astutify.mealplanner.recipe.presentation.list.RecipesFragment
import dagger.Component

@FeatureScope
@Component(
    modules = [RecipeModule::class],
    dependencies = [CoreComponent::class, RecipeOutNavigatorComponent::class]
)
interface RecipeComponent {

    @Component.Builder
    interface Builder {

        fun withCoreComponent(component: CoreComponent): Builder

        fun withNavigationComponent(component: RecipeOutNavigatorComponent): Builder

        fun build(): RecipeComponent
    }

    fun homeFragmentBuilder(): RecipesFragment.Component.Builder

    fun editRecipeActivityBuilder(): EditRecipeActivity.Component.Builder

    fun recipeDetailViewActivityBuilder(): RecipeDetailViewActivity.Component.Builder
}
