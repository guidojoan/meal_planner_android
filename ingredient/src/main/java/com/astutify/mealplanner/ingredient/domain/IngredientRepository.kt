package com.astutify.mealplanner.ingredient.domain

import com.astutify.mealplanner.core.model.domain.Ingredient
import io.reactivex.Single

interface IngredientRepository {

    fun saveIngredient(ingredient: Ingredient): Single<Ingredient>

    fun updateIngredient(ingredient: Ingredient): Single<Ingredient>

    fun getIngredientsFirstPage(keyWords: String?): Single<List<Ingredient>>

    fun getIngredientsNextPage(): Single<List<Ingredient>>
}
