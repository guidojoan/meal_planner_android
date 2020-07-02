package com.astutify.mealplanner.recipe.data

import com.astutify.mealplanner.core.data.CategoriesDataRepository
import com.astutify.mealplanner.core.data.MeasurementDataRepository
import com.astutify.mealplanner.core.model.data.RecipeApi
import com.astutify.mealplanner.core.model.data.mapper.toDomain
import com.astutify.mealplanner.core.model.domain.IngredientCategory
import com.astutify.mealplanner.core.model.domain.Measurement
import com.astutify.mealplanner.core.model.domain.Recipe
import com.astutify.mealplanner.core.model.domain.RecipeCategory
import com.astutify.mealplanner.core.model.domain.mapper.toData
import com.astutify.mealplanner.recipe.domain.RecipeRepository
import io.reactivex.Single
import io.reactivex.functions.Function3
import javax.inject.Inject

class RecipeDataRepository @Inject constructor(
    private val adminApiRepository: RecipeAdminApiRepository,
    private val recipesApiRepository: RecipesApiRepository,
    private val categoriesDataRepository: CategoriesDataRepository,
    private val measurementDataRepository: MeasurementDataRepository
) : RecipeRepository {

    override fun getRecipesFirstPage(keywords: String?) =
        mapRecipes(recipesApiRepository.firstPage(keywords))

    override fun getRecipesNextPage() =
        mapRecipes(recipesApiRepository.nextPage())

    override fun saveRecipe(recipe: Recipe) =
        mapRecipe(adminApiRepository.saveRecipe(recipe.toData()))

    override fun updateRecipe(recipe: Recipe) =
        mapRecipe(adminApiRepository.updateRecipe(recipe.toData()))

    override fun deleteRecipe(recipeId: String) =
        adminApiRepository.deleteRecipe(recipeId)

    private fun mapRecipes(recipes: Single<List<RecipeApi>>) =
        recipes
            .flatMap {
                Single.zip(
                    measurementDataRepository.getMeasurements(),
                    categoriesDataRepository.getRecipeCategories(),
                    categoriesDataRepository.getIngredientCategories(),
                    Function3 { m: List<Measurement>, rC: List<RecipeCategory>, iC: List<IngredientCategory> ->
                        it.map {
                            it.toDomain(m, rC, iC)
                        }
                    }
                )
            }

    private fun mapRecipe(recipe: Single<RecipeApi>) =
        recipe
            .flatMap {
                Single.zip(
                    measurementDataRepository.getMeasurements(),
                    categoriesDataRepository.getRecipeCategories(),
                    categoriesDataRepository.getIngredientCategories(),
                    Function3 { m: List<Measurement>, rC: List<RecipeCategory>, iC: List<IngredientCategory> ->
                        it.toDomain(m, rC, iC)
                    }
                )
            }
}
