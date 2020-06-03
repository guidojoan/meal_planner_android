package com.astutify.mealplanner.core.data

import com.astutify.mealplanner.core.entity.data.error.ApiErrorManager
import com.astutify.mealplanner.core.entity.data.mapper.toDomain
import io.reactivex.Single.error
import io.reactivex.Single.just
import javax.inject.Inject

class CategoriesApiRepository @Inject constructor(
    private val api: CategoriesApi,
    private val apiErrorManager: ApiErrorManager
) {

    fun getRecipeCategories() = api.getRecipeCategories()
        .flatMap {
            if (it.isSuccessful) {
                just(
                    it.body()!!.let { recipes ->
                        recipes.map { item ->
                            item.toDomain()
                        }
                    }
                )
            } else {
                error(apiErrorManager.mapError(it.code(), it.errorBody()?.string()))
            }
        }

    fun getIngredientCategories() =
        api.getIngredientCategories()
            .flatMap {
                if (it.isSuccessful) {
                    just(
                        it.body()!!.let { categories ->
                            categories.map { item ->
                                item.toDomain()
                            }
                        }
                    )
                } else {
                    error(apiErrorManager.mapError(it.code(), it.errorBody()?.string()))
                }
            }
}
