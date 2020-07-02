package com.astutify.mealplanner.ingredient

import com.astutify.mealplanner.core.FeatureScope
import com.astutify.mealplanner.core.data.CategoriesDataRepository
import com.astutify.mealplanner.core.data.MeasurementDataRepository
import com.astutify.mealplanner.ingredient.data.IngredientAdminApiRepository
import com.astutify.mealplanner.ingredient.data.IngredientDataRepository
import com.astutify.mealplanner.ingredient.data.IngredientsApi
import com.astutify.mealplanner.ingredient.data.IngredientsApiRepository
import com.astutify.mealplanner.ingredient.domain.IngredientRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class IngredientModule {

    @Provides
    @FeatureScope
    fun providesSearchRepository(
        adminApi: IngredientAdminApiRepository,
        ingredientsApiRepository: IngredientsApiRepository,
        categoriesDataRepository: CategoriesDataRepository,
        measurementDataRepository: MeasurementDataRepository
    ): IngredientRepository {
        return IngredientDataRepository(
            adminApi,
            ingredientsApiRepository,
            categoriesDataRepository,
            measurementDataRepository
        )
    }

    @Provides
    fun providesSearchApi(retrofit: Retrofit): IngredientsApi {
        return retrofit.create(IngredientsApi::class.java)
    }
}
