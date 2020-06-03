package com.astutify.mealplanner.coreui.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.UUID

@Parcelize
data class IngredientGroupViewModel(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val recipeIngredients: List<RecipeIngredientViewModel> = emptyList()
) : Parcelable {

    fun copyWithNewServings(servings: Int, newServings: Int): IngredientGroupViewModel {
        val recipeIngredientsUpdated = mutableListOf<RecipeIngredientViewModel>()
        recipeIngredients.forEach {
            recipeIngredientsUpdated.add(it.copyWithNewServings(servings, newServings))
        }
        return copy(recipeIngredients = recipeIngredientsUpdated)
    }
}
