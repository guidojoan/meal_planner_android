package com.astutify.mealplanner.recipe.presentation.edit.mvi

import android.os.Parcelable
import com.astutify.mealplanner.coreui.model.RecipeCategoryViewModel
import com.astutify.mealplanner.coreui.model.RecipeViewModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EditRecipeViewState(
    val recipe: RecipeViewModel = RecipeViewModel(),
    val saveEnabled: Boolean = false,
    val recipeCategories: List<RecipeCategoryViewModel>? = null,
    val mode: Mode = Mode.NEW,
    val loading: Loading? = null,
    val error: Error? = null,
    val message: Message? = null
) : Parcelable {

    fun copyState(
        recipe: RecipeViewModel = this.recipe,
        saveEnabled: Boolean = this.saveEnabled,
        recipeCategories: List<RecipeCategoryViewModel>? = this.recipeCategories,
        mode: Mode = this.mode,
        loading: Loading? = null,
        error: Error? = null,
        message: Message? = null
    ) = copy(
        recipe = recipe,
        saveEnabled = saveEnabled,
        recipeCategories = recipeCategories,
        mode = mode,
        loading = loading,
        error = error,
        message = message
    )

    enum class Loading {
        LOADING, SAVE
    }

    enum class Error {
        LOADING, SAVE, NAME_TAKEN, IMAGE_NOT_SELECTED
    }

    enum class Message {
        DELETE_ALERT
    }

    enum class Mode {
        NEW, EDIT
    }
}