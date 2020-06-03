package com.astutify.mealplanner.recipe.presentation.edit

import com.astutify.mealplanner.coreui.entity.RecipeCategoryViewModel
import com.astutify.mealplanner.coreui.entity.RecipeIngredientViewModel
import io.reactivex.Observable

interface EditRecipeView {

    fun render(viewState: EditRecipeViewState)

    val events: Observable<Intent>

    sealed class Intent {
        class NameChanged(val name: String) : Intent()
        class DirectionsChanged(val directions: String) : Intent()
        class ServingsChanged(val servings: Int) : Intent()
        class IngredientAdded(
            val recipeIngredient: RecipeIngredientViewModel,
            val ingredientGroupId: String
        ) : Intent()

        class IngredientGroupAdded(val name: String) : Intent()
        class CategorySelected(val recipeCategory: RecipeCategoryViewModel) : Intent()
        object SaveClick : Intent()
        class NewIngredientClicked(val ingredientGroupId: String) : Intent()
        class IngredientGroupRemoveClicked(val ingredientGroupId: String) : Intent()
        class IngredientRemoveClicked(val recipeIngredientId: String) : Intent()
        class ImagePicked(val imageUri: String) : Intent()
        object ClickBack : Intent()
        object DeleteClicked : Intent()
        object ConfirmDelete : Intent()
    }
}
