package com.astutify.mealplanner.coreui.presentation

import com.astutify.mealplanner.coreui.entity.IngredientCategoryViewModel
import com.astutify.mealplanner.coreui.entity.MeasurementViewModel
import com.astutify.mealplanner.coreui.entity.RecipeCategoryViewModel
import com.astutify.mealplanner.coreui.presentation.control.SingleSelectionView

abstract class SelectionItemsMapper {

    companion object {

        fun mapRecipeCategory(
            list: List<RecipeCategoryViewModel>,
            selectedId: String? = null
        ): List<SingleSelectionView.SelectionItem> {
            val result = mutableListOf<SingleSelectionView.SelectionItem>()
            list.forEach {
                result.add(SingleSelectionView.SelectionItem(it, it.id == selectedId))
            }
            return result.sortedByDescending { it.selected }
        }

        fun mapIngredientCategory(
            list: List<IngredientCategoryViewModel>,
            selectedId: String? = null
        ): List<SingleSelectionView.SelectionItem> {
            val result = mutableListOf<SingleSelectionView.SelectionItem>()
            list.forEach {
                result.add(SingleSelectionView.SelectionItem(it, it.id == selectedId))
            }
            return result.sortedByDescending { it.selected }
        }

        fun mapMeasurement(
            list: List<MeasurementViewModel>,
            selectedId: String? = null
        ): List<SingleSelectionView.SelectionItem> {
            val result = mutableListOf<SingleSelectionView.SelectionItem>()
            list.forEach {
                result.add(SingleSelectionView.SelectionItem(it, it.id == selectedId))
            }
            return result.sortedByDescending { it.selected }
        }
    }
}
