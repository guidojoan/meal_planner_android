package com.astutify.mealplanner.recipe.presentation.detail.mvi

import android.os.Parcelable
import com.astutify.mealplanner.coreui.model.RecipeViewModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RecipeDetailViewState(
    val recipe: RecipeViewModel
) : Parcelable