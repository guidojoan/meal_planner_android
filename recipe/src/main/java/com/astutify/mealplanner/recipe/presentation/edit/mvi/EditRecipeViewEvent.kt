package com.astutify.mealplanner.recipe.presentation.edit.mvi

import com.astutify.mealplanner.coreui.model.RecipeCategoryViewModel
import com.astutify.mealplanner.coreui.model.RecipeIngredientViewModel

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