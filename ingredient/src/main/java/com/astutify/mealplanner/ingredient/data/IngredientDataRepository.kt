package com.astutify.mealplanner.ingredient.data

import com.astutify.mealplanner.core.data.CategoriesDataRepository
import com.astutify.mealplanner.core.data.MeasurementDataRepository
import com.astutify.mealplanner.core.entity.data.IngredientEntity
import com.astutify.mealplanner.core.entity.data.mapper.toDomain
import com.astutify.mealplanner.core.entity.domain.Ingredient
import com.astutify.mealplanner.core.entity.domain.IngredientCategory
import com.astutify.mealplanner.core.entity.domain.Measurement
import com.astutify.mealplanner.core.entity.domain.mapper.toData
import com.astutify.mealplanner.ingredient.domain.IngredientRepository
import io.reactivex.Single
import io.reactivex.Single.zip
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class IngredientDataRepository @Inject constructor(
    private val adminApiRepository: IngredientAdminApiRepository,
    private val ingredientsApiRepository: IngredientsApiRepository,
    private val categoriesDataRepository: CategoriesDataRepository,
    private val measurementDataRepository: MeasurementDataRepository
) : IngredientRepository {

    override fun saveIngredient(ingredient: Ingredient) =
        mapIngredient(adminApiRepository.saveIngredient(ingredient.toData()))

    override fun updateIngredient(ingredient: Ingredient) =
        mapIngredient(adminApiRepository.updateIngredient(ingredient.toData()))

    override fun getIngredientsFirstPage(keyWords: String?) =
        mapIngredients(ingredientsApiRepository.firstPage(keyWords))

    override fun getIngredientsNextPage() =
        mapIngredients(ingredientsApiRepository.nextPage())

    private fun mapIngredients(ingredients: Single<List<IngredientEntity>>) =
        ingredients
            .flatMap {
                zip(
                    categoriesDataRepository.getIngredientCategories(),
                    measurementDataRepository.getMeasurements(),
                    BiFunction { categories: List<IngredientCategory>, measurements: List<Measurement> ->
                        it.map { it.toDomain(measurements, categories) }
                    }
                )
            }

    private fun mapIngredient(ingredient: Single<IngredientEntity>) =
        ingredient
            .flatMap {
                zip(
                    categoriesDataRepository.getIngredientCategories(),
                    measurementDataRepository.getMeasurements(),
                    BiFunction { categories: List<IngredientCategory>, measurements: List<Measurement> ->
                        it.toDomain(measurements, categories)
                    }
                )
            }
}
