package com.astutify.mealplanner.ingredient.presentation.recipeingredient

import com.astutify.mealplanner.coreui.entity.IngredientViewModel
import com.astutify.mealplanner.coreui.entity.MeasurementViewModel
import io.reactivex.Observable

interface RecipeIngredientsView {

    fun render(viewState: RecipeIngredientsViewState)

    val events: Observable<Intent>

    sealed class Intent {
        class Search(val name: String) : Intent()
        class IngredientClick(val ingredient: IngredientViewModel) : Intent()
        class IngredientQuantitySet(val quantity: Float, val measurement: MeasurementViewModel) :
            Intent()

        object ClickBack : Intent()
        object ClickAddIngredient : Intent()
        object EnOfListReached : Intent()
    }
}
