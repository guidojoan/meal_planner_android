package com.astutify.mealplanner.core.data

import com.astutify.mealplanner.core.domain.CategoriesLocalRepository
import com.astutify.mealplanner.core.domain.CategoriesRepository
import com.astutify.mealplanner.core.model.domain.IngredientCategory
import com.astutify.mealplanner.core.model.domain.RecipeCategory
import io.reactivex.Single
import javax.inject.Inject

class CategoriesDataRepository @Inject constructor(
    private val apiRepository: CategoriesApiRepository,
    private val localRepository: CategoriesLocalRepository
) : CategoriesRepository {

    override fun getIngredientCategories(): Single<List<IngredientCategory>> {
        val localCategories = localRepository.getIngredientCategories()
        return if (localCategories != null) {
            Single.just(localCategories)
        } else {
            apiRepository.getIngredientCategories()
                .map {
                    localRepository.storeIngredientCategories(it)
                    it
                }
        }
    }

    override fun getRecipeCategories(): Single<List<RecipeCategory>> {
        val localCategories = localRepository.getRecipeCategories()
        return if (localCategories != null) {
            Single.just(localCategories)
        } else {
            apiRepository.getRecipeCategories()
                .map {
                    localRepository.storeRecipeCategories(it)
                    it
                }
        }
    }
}
