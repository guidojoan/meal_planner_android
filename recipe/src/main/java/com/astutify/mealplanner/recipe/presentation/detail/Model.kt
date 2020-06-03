package com.astutify.mealplanner.recipe.presentation.detail

import android.os.Parcelable
import com.astutify.mealplanner.coreui.entity.RecipeViewModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RecipeDetailViewState(
    val recipe: RecipeViewModel
) : Parcelable

sealed class RecipeDetailViewEvent {
    object ClickBack : RecipeDetailViewEvent()
    data class ServingsChanged(val servings: Int) : RecipeDetailViewEvent()
}

sealed class RecipeDetailViewEffect {
    object GoBack : RecipeDetailViewEffect()
}
