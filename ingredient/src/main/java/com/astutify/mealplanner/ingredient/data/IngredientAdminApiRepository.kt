package com.astutify.mealplanner.ingredient.data

import com.astutify.mealplanner.core.entity.data.IngredientEntity
import com.astutify.mealplanner.core.entity.data.error.ApiErrorManager
import io.reactivex.Single.error
import io.reactivex.Single.just
import javax.inject.Inject

class IngredientAdminApiRepository @Inject constructor(
    private val api: IngredientApi,
    private val apiErrorManager: ApiErrorManager
) {

    fun saveIngredient(ingredient: IngredientEntity) = api.saveIngredient(ingredient)
        .flatMap {
            if (it.isSuccessful) {
                just(it.body()!!)
            } else {
                error(apiErrorManager.mapError(it.code(), it.errorBody()?.string()))
            }
        }

    fun updateIngredient(ingredient: IngredientEntity) = api.updateIngredient(ingredient)
        .flatMap {
            if (it.isSuccessful) {
                just(it.body()!!)
            } else {
                error(apiErrorManager.mapError(it.code(), it.errorBody()?.string()))
            }
        }
}
