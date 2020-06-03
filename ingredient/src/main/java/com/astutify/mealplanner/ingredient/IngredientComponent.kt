package com.astutify.mealplanner.ingredient

import com.astutify.mealplanner.core.CoreComponent
import com.astutify.mealplanner.core.FeatureScope
import com.astutify.mealplanner.ingredient.presentation.editingredient.EditIngredientActivity
import com.astutify.mealplanner.ingredient.presentation.list.IngredientsFragment
import com.astutify.mealplanner.ingredient.presentation.recipeingredient.RecipeIngredientActivity
import dagger.Component

@FeatureScope
@Component(modules = [IngredientModule::class], dependencies = [CoreComponent::class])
interface IngredientComponent {

    @Component.Builder
    interface Builder {

        fun withCoreComponent(component: CoreComponent): Builder

        fun build(): IngredientComponent
    }

    fun ingredientsFragmentBuilder(): IngredientsFragment.Component.Builder

    fun editIngredientActivityBuilder(): EditIngredientActivity.Component.Builder

    fun recipeIngredientActivityBuilder(): RecipeIngredientActivity.Component.Builder
}
