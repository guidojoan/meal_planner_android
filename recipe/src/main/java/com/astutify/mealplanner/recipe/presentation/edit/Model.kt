package com.astutify.mealplanner.recipe.presentation.edit

import android.os.Parcelable
import com.astutify.mealplanner.coreui.model.RecipeCategoryViewModel
import com.astutify.mealplanner.coreui.model.RecipeIngredientViewModel
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

sealed class EditRecipeViewEvent {
    class NameChanged(val name: String) : EditRecipeViewEvent()
    class ServingsChanged(val servings: Int) : EditRecipeViewEvent()
    class IngredientGroupAdded(val name: String) : EditRecipeViewEvent()
    class IngredientAdded(
        val recipeIngredient: RecipeIngredientViewModel,
        val ingredientGroupId: String
    ) : EditRecipeViewEvent()

    class DirectionsChanged(val directions: String) : EditRecipeViewEvent()
    class CategorySelected(val recipeCategory: RecipeCategoryViewModel) : EditRecipeViewEvent()
    object ClickSave : EditRecipeViewEvent()
    class NewIngredientClicked(val ingredientGroupId: String) : EditRecipeViewEvent()
    object LoadData : EditRecipeViewEvent()
    object OnSaveError : EditRecipeViewEvent()
    object Loading : EditRecipeViewEvent()
    object LoadingError : EditRecipeViewEvent()
    object LoadingSave : EditRecipeViewEvent()
    class RecipeIngredientRemoveClick(val recipeIngredientId: String) : EditRecipeViewEvent()
    class IngredientGroupRemoveClick(val ingredientGroupId: String) : EditRecipeViewEvent()
    class ImagePicked(val imageUrl: String) : EditRecipeViewEvent()
    class DataLoaded(val recipeCategories: List<RecipeCategoryViewModel>) : EditRecipeViewEvent()
    object ClickBack : EditRecipeViewEvent()
    object ClickDelete : EditRecipeViewEvent()
    object ConfirmDelete : EditRecipeViewEvent()
    object ErrorNameTaken : EditRecipeViewEvent()
}

sealed class EditRecipeViewEffect {
    class SaveRecipe(val recipe: RecipeViewModel) : EditRecipeViewEffect()
    object LoadData : EditRecipeViewEffect()
    class GoToRecipeIngredients(val ingredientGroupId: String) : EditRecipeViewEffect()
    object GoBack : EditRecipeViewEffect()
    object Delete : EditRecipeViewEffect()
}
