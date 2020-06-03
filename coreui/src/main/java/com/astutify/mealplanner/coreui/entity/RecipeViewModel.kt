package com.astutify.mealplanner.coreui.entity

import android.os.Parcelable
import com.astutify.mealplanner.core.extension.EMPTY
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RecipeViewModel(
    val id: String = String.EMPTY,
    val name: String = String.EMPTY,
    val directions: String = String.EMPTY,
    val servings: Int = 4,
    val ingredientGroups: List<IngredientGroupViewModel> = emptyList(),
    val imageUrl: String = String.EMPTY,
    val recipeCategory: RecipeCategoryViewModel = RecipeCategoryViewModel(),
    val status: Status = Status.SEEN
) : Parcelable {

    fun copyWithNewServings(newServings: Int): RecipeViewModel {
        val ingredientGroupsUpdated = mutableListOf<IngredientGroupViewModel>()
        ingredientGroups.forEach {
            ingredientGroupsUpdated.add(it.copyWithNewServings(servings, newServings))
        }
        return copy(ingredientGroups = ingredientGroupsUpdated, servings = newServings)
    }

    fun copyWithName(name: String): RecipeViewModel {
        return copy(name = name)
    }

    fun copyWithServings(servings: Int): RecipeViewModel {
        return copy(servings = servings)
    }

    fun copyWithDirections(directions: String): RecipeViewModel {
        return copy(directions = directions)
    }

    fun copyWithIngredientGroup(ingredientGroupName: String): RecipeViewModel {
        return copy(
            ingredientGroups = ingredientGroups.toMutableList().apply {
                add(
                    IngredientGroupViewModel(
                        name = ingredientGroupName
                    )
                )
            }
        )
    }

    fun copyWithOutIngredientGroup(ingredientGroupId: String): RecipeViewModel {
        return copy(ingredientGroups = ingredientGroups.filterNot { it.id == ingredientGroupId })
    }

    fun copyWithOutIngredient(recipeIngredientId: String): RecipeViewModel {
        return copy(
            ingredientGroups = ingredientGroups.map {
                if (it.recipeIngredients.find { item -> item.id == recipeIngredientId } != null) {
                    it.copy(recipeIngredients = it.recipeIngredients.filterNot { ingredient -> ingredient.id == recipeIngredientId })
                } else {
                    it
                }
            }
        )
    }

    fun copyWithIngredient(
        ingredientGroupId: String,
        recipeIngredient: RecipeIngredientViewModel
    ): RecipeViewModel {
        return copy(
            ingredientGroups = ingredientGroups.map {
                if (it.id == ingredientGroupId) {
                    it.copy(
                        recipeIngredients = it.recipeIngredients.toMutableList().apply {
                            add(recipeIngredient)
                        }
                    )
                } else {
                    it
                }
            }
        )
    }

    fun copyWithSeenStatus() = copy(status = Status.SEEN)

    fun saveEnabled(): Boolean {
        return directions.isNotBlank() && name.isNotBlank() && ingredientsNotEmpty() && recipeCategory != null
    }

    private fun ingredientsNotEmpty(): Boolean {
        ingredientGroups.forEach {
            if (it.recipeIngredients.isNotEmpty()) {
                return true
            }
        }
        return false
    }

    enum class Status {
        NEW, UPDATED, SEEN, DELETED
    }
}
