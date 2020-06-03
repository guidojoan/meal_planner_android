package com.astutify.mealplanner.recipe

import com.astutify.mealplanner.core.FeatureScope
import com.astutify.mealplanner.core.data.CategoriesDataRepository
import com.astutify.mealplanner.core.data.MeasurementDataRepository
import com.astutify.mealplanner.recipe.data.RecipeAdminApiRepository
import com.astutify.mealplanner.recipe.data.RecipeApi
import com.astutify.mealplanner.recipe.data.RecipeDataRepository
import com.astutify.mealplanner.recipe.data.RecipesApiRepository
import com.astutify.mealplanner.recipe.domain.RecipeRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class RecipeModule {

    @Provides
    @FeatureScope
    fun providesRecipeRepository(
        adminApiRepository: RecipeAdminApiRepository,
        recipesApiRepository: RecipesApiRepository,
        categoriesDataRepository: CategoriesDataRepository,
        measurementDataRepository: MeasurementDataRepository
    ): RecipeRepository {
        return RecipeDataRepository(
            adminApiRepository,
            recipesApiRepository,
            categoriesDataRepository,
            measurementDataRepository
        )
    }

    @Provides
    fun providesRecipeApi(retrofit: Retrofit): RecipeApi {
        return retrofit.create(RecipeApi::class.java)
    }
}
