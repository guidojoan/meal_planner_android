package com.astutify.mealplanner.ingredient.data

import com.astutify.mealplanner.core.model.data.IngredientApi
import com.astutify.mealplanner.core.model.data.error.ApiErrorManager
import io.reactivex.Single.error
import io.reactivex.Single.just
import javax.inject.Inject

class IngredientAdminApiRepository @Inject constructor(
    private val api: IngredientsApi,
    private val apiErrorManager: ApiErrorManager
) {

    fun saveIngredient(ingredient: IngredientApi) = api.saveIngredient(ingredient)
        .flatMap {
            if (it.isSuccessful) {
                just(it.body()!!)
            } else {
                error(apiErrorManager.mapError(it.code(), it.errorBody()?.string()))
            }
        }

    fun updateIngredient(ingredient: IngredientApi) = api.updateIngredient(ingredient)
        .flatMap {
            if (it.isSuccessful) {
                just(it.body()!!)
            } else {
                error(apiErrorManager.mapError(it.code(), it.errorBody()?.string()))
            }
        }
}
